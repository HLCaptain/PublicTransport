package hu.bme.aut.publictransport

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import hu.bme.aut.publictransport.ui.theme.PublicTransportTheme

class ListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PublicTransportTheme {
                TypeOfTravelScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TypeOfTravelScreen() {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(
            onClick = {
                openTicketDetails(
                    context = context,
                    ticketType = DetailsActivity.BikeType
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            TravelTypeImage(
                painterResourceId = R.drawable.bikes,
                contentDescription = stringResource(R.string.bike),
            )
            TravelTypeText(stringResource(R.string.bike))
        }
        IconButton(
            onClick = {
                openTicketDetails(
                    context = context,
                    ticketType = DetailsActivity.BusType
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            TravelTypeImage(
                painterResourceId = R.drawable.bus,
                contentDescription = stringResource(R.string.bus),
            )
            TravelTypeText(stringResource(R.string.bus))
        }
        IconButton(
            onClick = {
                openTicketDetails(
                    context = context,
                    ticketType = DetailsActivity.TrainType
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            TravelTypeImage(
                painterResourceId = R.drawable.trains,
                contentDescription = stringResource(R.string.train),
            )
            TravelTypeText(stringResource(R.string.train))
        }
        IconButton(
            onClick = {
                openTicketDetails(
                    context = context,
                    ticketType = DetailsActivity.BoatType
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            TravelTypeImage(
                painterResourceId = R.drawable.boat,
                contentDescription = stringResource(R.string.boat),
            )
            TravelTypeText(stringResource(R.string.boat))
        }
    }
}

fun openTicketDetails(
    context: Context,
    ticketType: Int = DetailsActivity.UnknownType
) {
    val intent = Intent(context, DetailsActivity::class.java)
        .putExtra(
            DetailsActivity.TicketTypeKey,
            ticketType
        )
    context.startActivity(intent)
}

@Composable
fun TravelTypeImage(
    painterResourceId: Int,
    contentDescription: String,
) {
    Image(
        painter = painterResource(id = painterResourceId),
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun TravelTypeText(text: String) {
    Text(
        text = text,
        color = Color.White,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineLarge
    )
}
