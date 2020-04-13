package com.scudderapps.moviesup

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.ms.square.android.expandabletextview.ExpandableTextView
import com.scudderapps.moviesup.adapter.TextViewAdapter
import com.scudderapps.moviesup.api.IMAGE_BASE_URL
import com.scudderapps.moviesup.api.TheTMDBApiInterface
import com.scudderapps.moviesup.api.TheTMDBClient
import com.scudderapps.moviesup.models.PeopleDetails
import com.scudderapps.moviesup.repository.peopledetails.PeopleDetailRepository
import com.scudderapps.moviesup.viewmodel.PeopleViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class PeopleDetailActivity : AppCompatActivity() {

    @BindView(R.id.people_name)
    lateinit var peopleName: TextView

    @BindView(R.id.people_image)
    lateinit var peopleImage: ImageView

    @BindView(R.id.people_department)
    lateinit var peopleDepartment: TextView

    @BindView(R.id.people_birthdate)
    lateinit var peopleBirthdate: TextView

    @BindView(R.id.people_birthplace)
    lateinit var peopleBirthplace: TextView

    @BindView(R.id.also_known_as_view)
    lateinit var alsoKnowAsView: RecyclerView

    @BindView(R.id.people_bio)
    lateinit var peopleBiography: ExpandableTextView


    @BindView(R.id.people_toolbar)
    lateinit var peopleToolbar: Toolbar

    lateinit var peopleDetailRepository: PeopleDetailRepository
    private lateinit var peopleViewModel: PeopleViewModel
    lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var textViewAdapter: TextViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.people_detail_activity)
        ButterKnife.bind(this)

        setSupportActionBar(peopleToolbar)
        supportActionBar!!.title = ("")

        val peopleDetails = intent.extras
        val id: Int = peopleDetails!!.getInt("id")

        val apiService: TheTMDBApiInterface = TheTMDBClient.getClient()
        peopleDetailRepository = PeopleDetailRepository(apiService)
        peopleViewModel = getViewMode(id)

        peopleViewModel.peopleDetails.observe(this, Observer {
            bindUI(it)
        })

    }

    fun bindUI(it: PeopleDetails) {

        if (!it.birthday.isNullOrEmpty()) {
            val originalFormat: DateFormat = SimpleDateFormat("yyyy-MM-d", Locale.ENGLISH)
            val targetFormat: DateFormat = SimpleDateFormat(getString(R.string.dateFormat))
            val date: Date = originalFormat.parse(it.birthday)
            val formattedDate: String = targetFormat.format(date)
            peopleBirthdate.text = "Date of birth: $formattedDate"
        } else {
            peopleBirthdate.visibility = View.GONE
        }

        if (!it.placeOfBirth.isNullOrEmpty()) {
            peopleBirthplace.text = "Born : " + it.placeOfBirth
        } else {
            peopleBirthplace.visibility = View.GONE
        }
        peopleName.text = it.name
        peopleBiography.text = it.biography
        var profilePath = it.profilePath
        val finalPath = IMAGE_BASE_URL + profilePath
        peopleDepartment.text = "Department: " + it.knownForDepartment
        Glide.with(this).load(finalPath).into(peopleImage)

        Log.v("list", it.alsoKnownAs.toString())
        textViewAdapter = TextViewAdapter(it.alsoKnownAs, this)
        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        alsoKnowAsView.layoutManager = linearLayoutManager
        alsoKnowAsView.setHasFixedSize(true)
        alsoKnowAsView.adapter = textViewAdapter

    }

    private fun getViewMode(peopleID: Int): PeopleViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return PeopleViewModel(peopleDetailRepository, peopleID) as T
            }
        })[PeopleViewModel::class.java]
    }
}
