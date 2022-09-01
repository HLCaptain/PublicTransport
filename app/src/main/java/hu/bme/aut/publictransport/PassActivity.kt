package hu.bme.aut.publictransport

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.bme.aut.publictransport.ui.theme.PublicTransportTheme

class PassActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PublicTransportTheme {
                DetailsActivity.apply {
                    val travelType = when (
                        intent.getIntExtra(
                            TravelTypeKey,
                            UnknownType
                        )
                    ) {
                        BusType -> getString(R.string.bus_pass)
                        BikeType -> getString(R.string.bike_pass)
                        BoatType -> getString(R.string.boat_pass)
                        TrainType -> getString(R.string.train_pass)
                        else -> getString(R.string.unknown_pass_type)
                    }
                    PassScreen(
                        passType = travelType,
                        passDate = intent.getStringExtra(DateKey) ?: ""
                    )
                }
            }
        }
    }

    companion object {
        const val DateKey = "KEY_DATE"
        const val TravelTypeKey = "KEY_TRAVEL_TYPE"
    }
}

@Preview(showBackground = true)
@Composable
fun PassScreen(
    modifier: Modifier = Modifier,
    passType: String = stringResource(R.string.unknown_pass_type),
    passDate: String = stringResource(R.string.start_date_end_date),
) {
    // TODO: Paste code here from README.md
}
