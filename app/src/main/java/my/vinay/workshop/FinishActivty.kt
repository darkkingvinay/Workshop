package my.vinay.workshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import my.vinay.workshop.databinding.ActivityFinishBinding

class FinishActivty : AppCompatActivity() {

    private var binding : ActivityFinishBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            binding = ActivityFinishBinding.inflate(layoutInflater)

            setContentView(binding?.root)
            setSupportActionBar(binding?.toolbarFinishActivity)
            if (supportActionBar != null) {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
            binding?.toolbarFinishActivity?.setNavigationOnClickListener {
                onBackPressed()
            }
            //END


            //START
            binding?.btnFinish?.setOnClickListener {
                finish()
            }
        }
    }
