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

    // Companion object is at the bottom, as told in (the bible)
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
    val nameResourceId: Int,
)

@Preview(showBackground = true)
@Composable
fun TypeOfTravelScreen() {
    // A Column would be fine as well, but we try
    // to reduce boilerplate code as much as possible.
    // That is why we made a list containing the travel types.
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        userScrollEnabled = false
    ) {
        items(ListActivity.travelTypes) {
            TravelTypeButton(
                modifier = Modifier
                    .fillMaxWidth()
                    // fillMaxSize() does not work here, because of the LazyColumn
                    // not supporting it properly. It instead supports this:
                    .fillParentMaxHeight(1f / ListActivity.travelTypes.size),
                ticketType = it.ticketType,
                imageResourceId = it.imageResourceId,
                nameResourceId = it.nameResourceId
            )
        }
    }
}

// Example comment using Kotlin features. It is best not to overuse comments.
// Try writing code which documents itself and only comment when it is necessary.
/**
 * Creates a button with the background image of [imageResourceId],
 * and a label in the middle with the string of [nameResourceId].
 * Pressing the button will start [DetailsActivity] with the given
 * [context]. The [ticketType] is put into the [Intent] beforehand.
 */
@Composable
fun TravelTypeButton(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    ticketType: Int = DetailsActivity.UnknownType,
    imageResourceId: Int = R.drawable.splash_image,
    nameResourceId: Int = R.string.unknown_ticket_type,
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
        TravelTypeText(text = stringResource(nameResourceId))
    }
}

// A more Java-like comment with KDoc!
// More formal, than above, but don't overuse it!
/**
 * Starts [DetailsActivity] with the given [context]
 * and the [ticketType] is put into the [Intent] beforehand.
 *
 * @param context starts [DetailsActivity].
 * @param ticketType will be put into the [Intent]
 * which starts [DetailsActivity].
 */
fun openTicketDetails(
    context: Context,
    ticketType: Int = DetailsActivity.UnknownType,
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
    modifier: Modifier = Modifier,
    painterResourceId: Int = R.drawable.splash_image,
    contentDescription: String = stringResource(R.string.unknown_ticket_type),
) {
    Image(
        painter = painterResource(painterResourceId),
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
fun TravelTypeText(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.unknown_ticket_type),
) {
    Text(
        text = text,
        color = Color.White,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineLarge,
        modifier = modifier
    )
}
