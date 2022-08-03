package hu.bme.aut.publictransport

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import hu.bme.aut.publictransport.ui.theme.PublicTransportTheme

class PassActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PublicTransportTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DetailsActivity.apply {
                        val travelType = when (intent.getIntExtra(
                            TravelTypeKey,
                            UnknownType
                        )) {
                            BusType -> getString(R.string.bus_pass)
                            BikeType -> getString(R.string.bike_pass)
                            BoatType -> getString(R.string.boat_pass)
                            TrainType -> getString(R.string.train_pass)
                            else -> getString(R.string.unknown_pass_type)
                        }
                        PassScreen(
                            travelType,
                            intent.getStringExtra(DateKey) ?: ""
                        )
                    }
                }
            }
        }
    }

    companion object {
        const val DateKey = "DateKey"
        const val TravelTypeKey = "TravelTypeKey"
    }
}

@Preview(showBackground = true)
@Composable
fun PassScreen(
    passType: String = "Type of pass",
    passDate: String = "Start date - End date"
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(
                state = scrollState,
                orientation = Orientation.Vertical
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = passType)
        Text(text = passDate)
        Image(
            painter = painterResource(id = R.drawable.qrcode),
            contentDescription = stringResource(R.string.qr_code)
        )
    }
}