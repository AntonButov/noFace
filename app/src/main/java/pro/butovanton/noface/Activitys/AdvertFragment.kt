package pro.butovanton.noface.Activitys

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import pro.butovanton.noface.R
import pro.butovanton.noface.viewmodels.AdvertViewModel


class AdvertFragment : Fragment() {

    private val model: AdvertViewModel by viewModels()

    lateinit var adverWeekB : Button
    lateinit var adverMonthB : Button
    lateinit var adverSixMonthB : Button
    lateinit var adverYearB : Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_advert, container, false)

        adverWeekB = root.findViewById<Button>(R.id.adveWeekBT)
        adverWeekB.setOnClickListener {
            model.launchBilling(requireActivity(), 0)
        }
        adverMonthB = root.findViewById<Button>(R.id.adveMonthBT)
        adverMonthB.setOnClickListener {
            model.launchBilling(requireActivity(), 1)
        }
        adverSixMonthB = root.findViewById<Button>(R.id.adveSixWeekBT)
        adverSixMonthB.setOnClickListener {
            model.launchBilling(requireActivity(), 2)
        }
        adverYearB = root.findViewById<Button>(R.id.adveYearBT)
        adverYearB.setOnClickListener {
            model.launchBilling(requireActivity(), 3)
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        if (model.getIsAdvertDontShow()) {
            adverWeekB.visibility = View.INVISIBLE
            adverMonthB.visibility = View.INVISIBLE
            adverSixMonthB.visibility = View.INVISIBLE
            adverYearB.visibility = View.INVISIBLE
        }
        else {
            adverWeekB.visibility = View.VISIBLE
            adverMonthB.visibility = View.VISIBLE
            adverSixMonthB.visibility = View.VISIBLE
            adverYearB.visibility = View.VISIBLE
        }
    }
}