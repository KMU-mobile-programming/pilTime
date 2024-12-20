// RegisterActivity.kt
package com.example.piltime

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.piltime.Database.DataBase  // 정확한 경로로 import
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    var DB: DataBase? = null  // DataBase로 변경
    lateinit var editTextId: EditText
    lateinit var editTextPassword: EditText
    lateinit var editTextRePassword: EditText
    lateinit var editTextNick: EditText
    lateinit var editTextPhone: EditText
    lateinit var btnRegister: Button
    lateinit var btnCheckId: Button
    var CheckId:Boolean = false
    lateinit var btnCheckNick: Button
    var CheckNick:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        DB = DataBase(this)  // DataBase로 변경
        editTextId = findViewById(R.id.editTextId_Reg)
        editTextPassword = findViewById(R.id.editTextPass_Reg)
        editTextRePassword = findViewById(R.id.editTextRePass_Reg)
        editTextNick = findViewById(R.id.editTextNick_Reg)
        editTextPhone = findViewById(R.id.editTextPhone_Reg)
        btnRegister = findViewById(R.id.btnRegister_Reg)
        btnCheckId = findViewById(R.id.btnCheckId_Reg)
        btnCheckNick = findViewById(R.id.btnCheckNick_Reg)

        // 아이디 중복확인
        btnCheckId.setOnClickListener {
            val user = editTextId.text.toString()
            val idPattern = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{6,15}$"

            if (user == "") {
                Toast.makeText(
                    this@RegisterActivity,
                    "아이디를 입력해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else {
                if (Pattern.matches(idPattern, user)) {
                    val checkUsername = DB!!.checkUser(user)
                    if(checkUsername == false){
                        CheckId = true
                        Toast.makeText(this@RegisterActivity, "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(this@RegisterActivity, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    Toast.makeText(this@RegisterActivity, "아이디 형식이 옳지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 닉네임 중복확인
        btnCheckNick.setOnClickListener {
            val nick = editTextNick.text.toString()
            val nickPattern = "^[가-힣a-zA-Z0-9]{2,10}$"

            if (nick == "") {
                Toast.makeText(
                    this@RegisterActivity,
                    "닉네임을 입력해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else {
                if (Pattern.matches(nickPattern, nick)) {
                    val checkNick = DB!!.checkNick(nick)
                    if(checkNick == false){
                        CheckNick = true
                        Toast.makeText(this@RegisterActivity, "사용 가능한 닉네임입니다.", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(this@RegisterActivity, "이미 존재하는 닉네임입니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    Toast.makeText(this@RegisterActivity, "닉네임 형식이 옳지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 완료 버튼 클릭 시
        btnRegister.setOnClickListener {
            val user = editTextId.text.toString()
            val pass = editTextPassword.text.toString()
            val repass = editTextRePassword.text.toString()
            val nick = editTextNick.text.toString()
            val phone = editTextPhone.text.toString()
            val pwPattern = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z0-9@!]{8,15}$"
            val phonePattern = "^(\\+[0-9]+)?[0-9]{10,15}$"

            if (user == "" || pass == "" || repass == "" || nick == "" || phone == "") {
                Toast.makeText(this@RegisterActivity, "회원정보를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                if (CheckId == true) {
                    if (Pattern.matches(pwPattern, pass)) {
                        if (pass == repass) {
                            if (CheckNick == true) {
                                if (Pattern.matches(phonePattern, phone)) {
                                    val insert = DB!!.insertUser(user, pass, nick, phone)  // insertData를 insertUser로 변경
                                    if (insert == true) {
                                        Toast.makeText(this@RegisterActivity, "가입되었습니다.", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                        startActivity(intent)
                                    } else {
                                        Toast.makeText(this@RegisterActivity, "가입 실패하였습니다.", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(this@RegisterActivity, "전화번호 형식이 옳지 않습니다.", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(this@RegisterActivity, "닉네임 중복확인을 해주세요.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@RegisterActivity, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@RegisterActivity, "비밀번호는 영문, 숫자, 특수문자(@#$%^&+=!)를 포함한 8-15자여야 합니다.",
                            Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@RegisterActivity, "아이디 중복확인을 해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}