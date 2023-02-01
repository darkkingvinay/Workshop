package my.vinay.workshop

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import my.vinay.workshop.databinding.ActivityExerciseBinding
import my.vinay.workshop.databinding.DialogCustomBackConfirmationBinding
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding : ActivityExerciseBinding? = null

    private var resttimer : CountDownTimer? =null
    private var restprogress = 0
    private var resttimeduration : Long = 5

    private var exercisetimer : CountDownTimer? = null
    private var exerciseprogress = 0
    private var exerciseTimerDuration: Long = 30

    private var exerciselist : ArrayList<ExerciseModel>? = null
    private var currentexerpos = -1

    private var tts: TextToSpeech? = null
     private var player : MediaPlayer? = null

    private var exerciseadapter : Exercisestatusadapter? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarExercise)
        if(supportActionBar !=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }


        binding?.toolbarExercise?.setNavigationOnClickListener{
            customDialogforBackbutton()
        }
        exerciselist = Constants.defaultExerciseList()
        tts = TextToSpeech(this, this)
        setuprestview()
        setexercisestatusrecycler()
    }



    override fun onBackPressed() {
        customDialogforBackbutton()
    }
    private fun customDialogforBackbutton(){
        val customdialog = Dialog(this)
        val dialogbinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customdialog.setContentView(dialogbinding.root)
        customdialog.setCanceledOnTouchOutside(false)
        dialogbinding.tvYes.setOnClickListener{
            this@ExerciseActivity.finish()
            customdialog.dismiss()
        }
        dialogbinding.tvNo.setOnClickListener{
            customdialog.dismiss()
        }
        customdialog.show()

    }

    private fun setexercisestatusrecycler(){
        binding?.rvexercisestatus?.layoutManager = LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL, false)
        exerciseadapter  = Exercisestatusadapter(exerciselist!!)
        binding?.rvexercisestatus?.adapter = exerciseadapter
    }
    private fun setuprestview(){
        try {
            val soundd = (Uri.parse("android.resource://my.vinay.workshop/"+ R.raw.app_src_main_res_raw_press_start))
            player = MediaPlayer.create(applicationContext,soundd)
            player?.isLooping = false
            player?.start()
        }catch (e: Exception){
            e.printStackTrace()
        }


        binding?.fiprogress?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility= View.VISIBLE
        binding?.tvExercise?.visibility = View.INVISIBLE
        binding?.fiexercise?.visibility = View.INVISIBLE
        binding?.ivimage?.visibility = View.INVISIBLE
        binding?.tvupcoming?.visibility = View.VISIBLE
        binding?.tvupcomingexercise?.visibility = View.VISIBLE
        if(resttimer!=null) {
            resttimer?.cancel()
            restprogress = 0
        }
        binding?.tvupcomingexercise?.text = exerciselist!![currentexerpos +1].getName()
        setrestprogressbar()
    }

  private fun setrestprogressbar(){
      binding?.progressbar?.progress = restprogress
      resttimer = object : CountDownTimer(resttimeduration * 1000,1000){
          override fun onTick(millisUntilFinished: Long)
          {
              restprogress++
              binding?.progressbar?.progress = 5 -restprogress
              binding?.tvTimer?.text = (5-restprogress).toString()
          }

          override fun onFinish() {
              currentexerpos++
              exerciselist!![currentexerpos].setIsSelected(true)
              exerciseadapter!!.notifyDataSetChanged()
              setupexerciseview()
          }

      }.start()
  }
    private fun setupexerciseview()
    {
        binding?.fiprogress?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility= View.INVISIBLE
        binding?.tvExercise?.visibility = View.VISIBLE
        binding?.fiexercise?.visibility = View.VISIBLE
        binding?.ivimage?.visibility = View.VISIBLE
        binding?.tvupcoming?.visibility = View.INVISIBLE
        binding?.tvupcomingexercise?.visibility = View.INVISIBLE

        if(exercisetimer!=null){
            exercisetimer?.cancel()
            exerciseprogress =0
        }

        speakOut(exerciselist!![currentexerpos].getName())
        binding?.ivimage?.setImageResource(exerciselist!![currentexerpos].getImage())
        binding?.tvExercise?.text = exerciselist!![currentexerpos].getName()

        setexerciseprogressbar()
    }
    private fun setexerciseprogressbar(){
        binding?.progressbarexercise?.progress = exerciseprogress
        exercisetimer = object : CountDownTimer(exerciseTimerDuration * 1000,1000){
            override fun onTick(millisUntilFinished: Long)
            {
                exerciseprogress++
                binding?.progressbarexercise?.progress = (exerciseTimerDuration.toInt()) - exerciseprogress
                binding?.tvTimerexercise?.text = (exerciseTimerDuration.toInt()-exerciseprogress).toString()
            }

            override fun onFinish() {


               if(currentexerpos <exerciselist?.size!! -1){
                   exerciselist!![currentexerpos].setIsSelected(false)
                   exerciselist!![currentexerpos].setIsCompleted(true)
                   exerciseadapter!!.notifyDataSetChanged()
                   setuprestview()
               }else{
                  finish()
                   val intent = Intent(this@ExerciseActivity ,FinishActivty::class.java)
                   startActivity(intent)
               }
            }

        }.start()
    }

    override fun onDestroy() {
        if(resttimer!=null){
            resttimer?.cancel()
            restprogress = 0
    }
        if(exercisetimer!=null){
            exercisetimer?.cancel()
            exerciseprogress =0
        }
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()}
        if(player !=null){
            player!!.stop()
        }
        super.onDestroy()
        binding = null
    }

    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts?.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported!")
            }

        } else {
            Log.e("TTS", "Initialization Failed!")
        }

    }
    private fun speakOut(text: String) {
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }


}




