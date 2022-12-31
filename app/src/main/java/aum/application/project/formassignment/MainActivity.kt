package aum.application.project.formassignment

import android.app.DatePickerDialog
import android.app.RemoteAction
import android.graphics.Color.blue
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import aum.application.project.formassignment.databinding.*
import aum.application.project.formassignment.models.Field
import aum.application.project.formassignment.models.FieldData
import aum.application.project.formassignment.models.Option
import aum.application.project.formassignment.models.SelectedFields
import aum.application.project.formassignment.utils.Fields
import aum.application.project.formassignment.utils.loadJSONFile
import com.google.gson.Gson
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var listId= ArrayList<SelectedFields>()

    var radioBtntxt:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



       val data= loadJSONFile(this,"Qnopy Test Fields.json")
        val gson = Gson()
        val fieldData: FieldData = gson.fromJson(data, FieldData::class.java)

        for (i in 0..fieldData.fields.size-1){
            onAddField(fieldData.fields[i], index = i)
        }


        binding.submitBtn.setOnClickListener {
            if (checkField()){
                Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"Failure",Toast.LENGTH_SHORT).show()

            }
        }
    }


    fun onAddField( field: Field,index: Int) {


        when(field.type){
            Fields.PICKER.toString()->{
                val fieldBinding=  SelectViewBinding.inflate(layoutInflater)
                binding.parentLinearLayout.addView(fieldBinding.root, binding.parentLinearLayout.childCount - 1)
                fieldBinding.fieldName.text=field.title
                setSpinner(fieldBinding.coursesspinner,field.options)

                fieldBinding.coursesspinner?.tag="tag$index"

                if (field.isRequiredField){ fieldBinding.fieldName.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_required_icon,0)


                   listId.add(SelectedFields(field.type, "tag$index"))

                }
                Log.d("id_field",fieldBinding.coursesspinner.id.toString())




            }
            Fields.TEXT.toString()->{
                val fieldBinding=  TextViewBinding.inflate(layoutInflater)
                binding.parentLinearLayout.addView(fieldBinding.root, binding.parentLinearLayout.childCount - 1)
                fieldBinding.fieldName.text=field.title
                fieldBinding.edTxt.editText?.tag="tag$index"

                if (field.isRequiredField) {
                    fieldBinding.fieldName.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_required_icon,0)
                   // listId.add(fieldBinding.edTxt.id)

                    listId.add(SelectedFields(field.type, "tag$index"))


                }




            }
            Fields.NUMERIC.toString()->{
                val fieldBinding=  TextViewBinding.inflate(layoutInflater)
                binding.parentLinearLayout.addView(fieldBinding.root, binding.parentLinearLayout.childCount - 1)
                fieldBinding.fieldName.text=field.title
                fieldBinding.fieldName.inputType = InputType.TYPE_CLASS_PHONE

                fieldBinding.edTxt.editText?.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED //for positive or negative values
                fieldBinding.edTxt.editText?.tag="tag$index"


                if (field.isRequiredField) {

                    fieldBinding.fieldName.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_required_icon,
                        0
                    )

                    listId.add(SelectedFields(field.type, "tag$index"))


                }
              //  fieldBinding.fieldName.id=5


                Log.d("id_field",fieldBinding.edTxt.id.toString())

            }
            Fields.RADIOBUTTON.toString()->{
                val fieldBinding=  RadioViewBinding.inflate(layoutInflater)
                binding.parentLinearLayout.addView(fieldBinding.root, binding.parentLinearLayout.childCount - 1)

                fieldBinding.fieldName.text=field.title
                fieldBinding.radioGroup?.tag="tag$index"

                for (i in 0 until field.options.size){
                    addRadioButton(i,fieldBinding.radioGroup,field.options[i].itemName)
                }

                if (field.isRequiredField) {

                    fieldBinding.fieldName.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_required_icon,
                        0
                    )

                    listId.add(SelectedFields(field.type, "tag$index"))

                }
                Log.d("id_field",fieldBinding.radioGroup.id.toString())
                if (fieldBinding.radioGroup !=null) {

                    fieldBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
                       radioBtntxt.toString()
                    }
                }
            }






            Fields.DATE.toString()->{
                val fieldBinding=  DateViewBinding.inflate(layoutInflater)
                binding.parentLinearLayout.addView(fieldBinding.root, binding.parentLinearLayout.childCount - 1)
                fieldBinding.fieldName.text=field.title

                fieldBinding.selectText.setOnClickListener {

                    addDatePicker(fieldBinding.selectText)
                }
                if (field.isRequiredField) {

                    fieldBinding.fieldName.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_required_icon,0)
                    listId.add(SelectedFields(field.type, "tag$index"))

                }
                Log.d("id_field",fieldBinding.selectText.id.toString())

            }
        }




    }

    private  fun setSpinner(spinner: Spinner,list: List<Option>){


        val adapter = ArrayAdapter(this, R.layout.spinner_item, list)
        spinner.adapter = adapter


        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

    }

    private  fun addRadioButton(index:Int, radioGroup: RadioGroup,name:String){
        val radioButton1 = RadioButton(this)
        radioButton1.layoutParams= LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        radioButton1.text = name
        radioButton1.id = index

        radioGroup.addView(radioButton1)

    }


    private fun addDatePicker(txtView: TextView?){
        // binding.imgDobCalender.setOnClickListener {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { view, year, monthOfYear, dayOfMonth ->

                val txt:String =  (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                txtView?.setText(txt)
            },
            // on below line we are passing year, month
            // and day for the selected date in our date picker.
            year,
            month,
            day
        )
        // at last we are calling show
        // to display our date picker dialog.
        datePickerDialog.show()

    }


  private  fun checkField( ):Boolean {

      var isValid=true

        for (i in listId){


            when(i.fieldType){
                Fields.TEXT.toString()->{
                    val txt = binding.parentLinearLayout.findViewWithTag<View>(i.fieldTag) as EditText

                    Log.d(i.fieldTag,txt.text.toString())
                    if (!txt.text.isNullOrEmpty()){
                        isValid= false
                    }

                }Fields.NUMERIC.toString()->{
                val txt = binding.parentLinearLayout.findViewWithTag<View>(i.fieldTag) as EditText
                Log.d(i.fieldTag,txt.text.toString())

                if (!txt.text.isNullOrEmpty()){
                    isValid= false
                }

            }Fields.DATE.toString()->{
                val txt = binding.parentLinearLayout.findViewWithTag<View>(i.fieldTag) as TextView
                Log.d(i.fieldTag,txt.text.toString())

                if (!txt.text.isNullOrEmpty()){
                    isValid= false
                }

            }Fields.RADIOBUTTON.toString()->{
                val txt = binding.parentLinearLayout.findViewWithTag<View>(i.fieldTag) as RadioGroup

                Log.d(i.fieldTag,txt.checkedRadioButtonId.toString())

                if (txt.checkedRadioButtonId == -1)
                {
                    isValid=  false
                    // no radio buttons are checked
                }


            }


            }

        }



        return isValid

    }


}





