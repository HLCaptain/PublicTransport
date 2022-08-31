package hu.bme.aut.publictransport

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.bme.aut.publictransport.ui.theme.PublicTransportTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PublicTransportTheme {
                LoginScreen()
            }
        }
    }
}

// Annotation needed to use TextField, Button, etc.
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun LoginScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        // There is 8.dp space between items in the Column
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.please_enter_your_credentials),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.titleMedium
        )

        val context = LocalContext.current

        var email by remember { mutableStateOf("") }
        var emailLabel by remember { mutableStateOf(context.getString(R.string.email_label)) }
        var wasEmailValidated by remember { mutableStateOf(false) }
        val isEmailWrong = email.isBlank() && wasEmailValidated
        TextField(
            value = email,
            onValueChange = {
                email = it
                emailLabel = context.getString(R.string.email_label)
            },
            // label accepts a Composable. Can be anything! The wonders, Compose is capable of 😊.
            label = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(emailLabel)
                    if (!isEmailWrong) {
                        Icon(
                            imageVector = Icons.Filled.Email,
                            contentDescription = stringResource(R.string.email_icon),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            singleLine = true,
            // Show error state, when statement is true
            isError = isEmailWrong
        )

        var password by remember { mutableStateOf("") }
        var passwordLabel by remember { mutableStateOf(context.getString(R.string.password_label)) }
        var wasPasswordValidated by remember { mutableStateOf(false) }
        val isPasswordWrong = password.isBlank() && wasPasswordValidated
        TextField(
            value = password,
            onValueChange = {
                password = it
                passwordLabel = context.getString(R.string.password_label)
            },
            label = { Text(passwordLabel) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            singleLine = true,
            isError = isPasswordWrong
        )

        Button(
            onClick = {
                // Validating text fields
                wasEmailValidated = true
                wasPasswordValidated = true
                val emailEmpty = email.isBlank()
                if (emailEmpty) {
                    emailLabel = context.getString(R.string.please_enter_your_email_address)
                }
                val passwordEmpty = password.isBlank()
                if (passwordEmpty) {
                    passwordLabel = context.getString(R.string.please_enter_your_password)
                }
                if (!emailEmpty && !passwordEmpty) {
                    context.startActivity(
                        Intent(
                            context,
                            ListActivity::class.java
                        )
                    )
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(R.string.login))
        }
    }
}
