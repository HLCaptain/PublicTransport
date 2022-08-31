package hu.bme.aut.publictransport

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

    // Companion object is at the bottom, as told in
    // https://kotlinlang.org/docs/coding-conventions.html
    companion object {
        val travelTypes = listOf(
            TravelType(
                ticketType = DetailsActivity.BikeType,
                imageResourceId = R.drawable.bikes,
                nameResourceId = R.string.bike
            ),
            TravelType(
                ticketType = DetailsActivity.BusType,
                imageResourceId = R.drawable.bus,
                nameResourceId = R.string.bus
            ),
            TravelType(
                ticketType = DetailsActivity.TrainType,
                imageResourceId = R.drawable.trains,
                nameResourceId = R.string.train
            ),
            TravelType(
                ticketType = DetailsActivity.BoatType,
                imageResourceId = R.drawable.boat,
                nameResourceId = R.string.boat
            ),
        )
    }
}

data class TravelType(
    val ticketType: Int,
    val imageResourceId: Int,
    val nameResourceId: Int
)

@Preview(showBackground = true)
@Composable
fun TypeOfTravelScreen() {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        userScrollEnabled = false
    ) {
        items(ListActivity.travelTypes) {
            TravelTypeButton(
                modifier =  Modifier
                    .fillMaxWidth()
                    .fillParentMaxHeight(1f / ListActivity.travelTypes.size),
                context = context,
                ticketType = it.ticketType,
                imageResourceId = it.imageResourceId,
                nameResourceId = it.nameResourceId
            )
        }
    }
}

@Composable
fun TravelTypeButton(
    modifier: Modifier = Modifier,
    context: Context,
    ticketType: Int,
    imageResourceId: Int,
    nameResourceId: Int
) {
    IconButton(
        onClick = {
            openTicketDetails(
                context = context,
                ticketType = ticketType
            )
        },
        modifier = modifier
    ) {
        TravelTypeImage(
            painterResourceId = imageResourceId,
            contentDescription = stringResource(nameResourceId),
        )
        TravelTypeText(stringResource(nameResourceId))
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
        painter = painterResource(painterResourceId),
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
