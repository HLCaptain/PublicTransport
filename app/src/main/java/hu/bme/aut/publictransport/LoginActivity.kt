package hu.bme.aut.publictransport

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.bme.aut.publictransport.ui.theme.PublicTransportTheme
import kotlin.reflect.typeOf

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PublicTransportTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
    ) {
        Text(
            text = "Please enter your credentials!",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        var email by remember { mutableStateOf("") }
        BasicTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            )
        )

        var password by remember { mutableStateOf("") }
        BasicTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            )
        )

        val context = LocalContext.current
        Button(
            onClick = {
                val emailEmpty = email.isEmpty()
                if (emailEmpty) {
                    email = context.getString(R.string.please_enter_your_email_address)
                }
                val passwordEmpty = password.isEmpty()
                if (passwordEmpty) {
                    password = context.getString(R.string.please_enter_your_password)
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
            Text(text = "Login")
        }
    }
}