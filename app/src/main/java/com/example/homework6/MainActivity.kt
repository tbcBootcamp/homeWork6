package com.example.homework6

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import com.example.homework6.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val userList = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvActive.text = "0"
        binding.tvRemoved.text = "0"

        binding.btnAdd.setOnClickListener {
            checkAndAdd()
        }
        binding.btnRemove.setOnClickListener {
            checkAndRemove()
        }
        binding.btnUpdate.setOnClickListener {
            checkAndUpdate()
        }
    }

    private fun checkAndUpdate() {
        if (isEmptyField() || !isEmailValid()) {
            updateResultText(getString(R.string.empty_fields_error), Color.RED)
        } else {
            val userInfo = getUserInfo()
            updateUser(userInfo)
        }
    }

    private fun checkAndRemove() {
        if (isEmptyField() || !isEmailValid()) {
            updateResultText(getString(R.string.empty_fields_error), Color.RED)
        } else {
            val email = binding.etEmail.text.toString()
            removeUserByEmail(email)
        }
    }

    private fun checkAndAdd() {
        if (isEmptyField() || !isEmailValid()) {
            updateResultText(getString(R.string.empty_fields_error), Color.RED)
        } else {
            val email = binding.etEmail.text.toString()
            val userInfo = getUserInfo()
            if (userList.any { it.email == email }) {
                updateResultText(getString(R.string.already_exists), Color.RED)
            } else {
                addUser(userInfo)
            }
        }
    }

    private fun addUser(userData: User) {
        userList.add(userData)
        clearFields()
        addToActiveUsersList()
        updateResultText(getString(R.string.add_successfully), Color.GREEN)
    }

    private fun removeUserByEmail(email: String) {
        val user = userList.find { it.email == email }
        if (user != null) {
            userList.remove(user)
            clearFields()
            addToRemovedUsersList()
            updateResultText(getString(R.string.removed_successfully), Color.GREEN)
        } else {
            updateResultText(getString(R.string.not_exists), Color.RED)
        }
    }

    private fun updateUser(userData: User) {
        val email = userData.email
        val existingUser = userList.find { it.email == email }
        if (existingUser != null) {
            existingUser.firstName = userData.firstName
            existingUser.lastName = userData.lastName
            existingUser.age = userData.age
            clearFields()
            updateResultText(getString(R.string.updated_successfully), Color.GREEN)
        } else {
            updateResultText(getString(R.string.not_exists), Color.RED)
        }
    }

    private fun updateResultText(message: String, textColor: Int) {
        binding.tvResult.text = message
        binding.tvResult.setTextColor(textColor)
    }

    private fun isEmailValid(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()
    }

    private fun isEmptyField(): Boolean = with(binding) {
        return etFirstName.text.toString().isEmpty() ||
                etLastName.text.toString().isEmpty() ||
                etAge.text.toString().isEmpty() ||
                etEmail.text.toString().isEmpty()
    }

    private fun clearFields() = with(binding) {
        etFirstName.text?.clear()
        etLastName.text?.clear()
        etAge.text?.clear()
        etEmail.text?.clear()
    }

    private fun addToActiveUsersList() {
        val activeUsers = binding.tvActive.text.toString().toInt()
        binding.tvActive.text = (activeUsers + 1).toString()
    }

    private fun addToRemovedUsersList() {
        val removedUsers = binding.tvRemoved.text.toString().toInt()
        val activeUsers = binding.tvActive.text.toString().toInt()
        binding.tvRemoved.text = (removedUsers + 1).toString()
        binding.tvActive.text = (activeUsers - 1).toString()
    }

    private fun getUserInfo(): User {
        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val age = binding.etAge.text.toString().toInt()
        val email = binding.etEmail.text.toString()
        return User(firstName, lastName, age, email)
    }
}