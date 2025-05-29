package com.aZina0.circulationmaze.mainMenu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aZina0.circulationmaze.R

@Composable
fun TitleComposable() {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val size = (screenWidthDp / 11).dp - 3.dp

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.c_letter),
                contentDescription = "img",
                modifier = Modifier
                    .size(size)
            )
            Image(
                painter = painterResource(id = R.drawable.i_piece),
                contentDescription = "img",
                modifier = Modifier
                    .size(size)
            )
            Image(
                painter = painterResource(id = R.drawable.l_piece),
                contentDescription = "img",
                modifier = Modifier
                    .size(size)
            )
            Image(
                painter = painterResource(id = R.drawable.c_letter),
                contentDescription = "img",
                modifier = Modifier
                    .size(size)
            )
            Image(
                painter = painterResource(id = R.drawable.u_letter),
                contentDescription = "img",
                modifier = Modifier
                    .size(size)
            )
            Image(
                painter = painterResource(id = R.drawable.l_piece),
                contentDescription = "img",
                modifier = Modifier
                    .size(size)
                    .rotate(270f)
            )
            Image(
                painter = painterResource(id = R.drawable.a_letter),
                contentDescription = "img",
                modifier = Modifier
                    .size(size)
            )
            Image(
                painter = painterResource(id = R.drawable.t_piece),
                contentDescription = "img",
                modifier = Modifier
                    .size(size)
                    .rotate(90f)
            )
            Image(
                painter = painterResource(id = R.drawable.i_piece),
                contentDescription = "img",
                modifier = Modifier
                    .size(size)
            )
            Image(
                painter = painterResource(id = R.drawable.o_letter),
                contentDescription = "img",
                modifier = Modifier
                    .size(size)
            )
            Image(
                painter = painterResource(id = R.drawable.n_letter),
                contentDescription = "img",
                modifier = Modifier
                    .size(size)
            )
        }
        Row {
            Image(
                painter = painterResource(id = R.drawable.m_letter),
                contentDescription = "img",
                modifier = Modifier
                    .size(size)
            )
            Image(
                painter = painterResource(id = R.drawable.a_letter),
                contentDescription = "img",
                modifier = Modifier
                    .size(size)
            )
            Image(
                painter = painterResource(id = R.drawable.z_letter),
                contentDescription = "img",
                modifier = Modifier
                    .size(size)
            )
            Image(
                painter = painterResource(id = R.drawable.e_letter),
                contentDescription = "img",
                modifier = Modifier
                    .size(size)
            )
        }
    }
}