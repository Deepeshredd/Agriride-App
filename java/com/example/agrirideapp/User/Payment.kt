package com.example.agrirideapp.User

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import com.example.agrirideapp.R
import dev.shreyaspatil.easyupipayment.EasyUpiPayment
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener
import dev.shreyaspatil.easyupipayment.model.PaymentApp
import dev.shreyaspatil.easyupipayment.model.TransactionDetails
import dev.shreyaspatil.easyupipayment.model.TransactionStatus

class Payment : AppCompatActivity(), PaymentStatusListener {
    lateinit var etamount:EditText
    lateinit var field_payee_merchant_code:EditText
    lateinit var ettranscation:EditText
    lateinit var etreqtransaction:EditText
    lateinit var etupiid1:EditText
    lateinit var etname:EditText
    lateinit var etdec:EditText
    lateinit var radioAppChoice:RadioGroup
    lateinit var app_default:RadioButton
    lateinit var app_google_pay:RadioButton
   lateinit var  app_phonepe:RadioButton
    lateinit var app_paytm:RadioButton
    lateinit var btnpayment:Button
    lateinit var tvustatus1:TextView
    private lateinit var easyUpiPayment: EasyUpiPayment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        etamount=findViewById(R.id.etamount)
        field_payee_merchant_code=findViewById(R.id.field_payee_merchant_code)
        ettranscation=findViewById(R.id.ettranscation)
        etreqtransaction=findViewById(R.id.etreqtransaction)
        etupiid1=findViewById(R.id.etupiid1)
        etname=findViewById(R.id.etname)
        etdec=findViewById(R.id.etdec)
        radioAppChoice=findViewById(R.id.radioAppChoice)
        app_default=findViewById(R.id.app_default)
        app_google_pay=findViewById(R.id.app_google_pay)
        app_phonepe=findViewById(R.id.app_phonepe)
        app_paytm=findViewById(R.id.app_paytm)
        btnpayment=findViewById(R.id.btnpayment)
        tvustatus1=findViewById(R.id.tvustatus1)


        btnpayment.setOnClickListener {
            initViews()

        }
    }
    private fun initViews() {
        val transactionId = "TID" + System.currentTimeMillis()
        ettranscation.setText(transactionId)
        etreqtransaction.setText(transactionId)
        btnpayment.setOnClickListener {pay()}
    }

    private fun pay() {
        val upi=etupiid1.text.toString()
        val amount=etamount.text.toString()
        val tran=ettranscation.text.toString()
        val tranreg=etreqtransaction.text.toString()
        val dec=etdec.text.toString()
        val nam=etname.text.toString()
        val payeeMerchantCode = field_payee_merchant_code.text.toString()
        val paymentAppChoice = radioAppChoice

        val paymentApp = when (paymentAppChoice.checkedRadioButtonId) {
            R.id.app_default -> PaymentApp.ALL
            R.id.app_google_pay -> PaymentApp.GOOGLE_PAY
            R.id.app_phonepe -> PaymentApp.PHONE_PE
            R.id.app_paytm -> PaymentApp.PAYTM
            else -> throw IllegalStateException("Unexpected value: " + paymentAppChoice.id)
        }

        try {
            // START PAYMENT INITIALIZATION
            easyUpiPayment = EasyUpiPayment(this) {
                this.paymentApp = paymentApp
                this.payeeVpa = upi
                this.payeeName = nam
                this.transactionId = tran
                this.transactionRefId = tranreg
                this.payeeMerchantCode = payeeMerchantCode
                this.description = dec
                this.amount = amount
            }
            easyUpiPayment.setPaymentStatusListener(this)
            easyUpiPayment.startPayment()
        } catch (e: Exception) {
            e.printStackTrace()

            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()

        }

    }
    override fun onTransactionCancelled() {
        toast("Cancelled by user")
    }

    override fun onTransactionCompleted(transactionDetails: TransactionDetails) {
        tvustatus1.text = transactionDetails.toString()

        when (transactionDetails.transactionStatus) {
            TransactionStatus.SUCCESS -> onTransactionSuccess()
            TransactionStatus.FAILURE -> onTransactionFailed()
            TransactionStatus.SUBMITTED -> onTransactionSubmitted()
        }
    }

    private fun onTransactionSubmitted() {
        toast("Pending | Submitted")
    }

    private fun onTransactionFailed() {
        toast("Failed")
    }

    private fun onTransactionSuccess() {
        toast("Success")
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}