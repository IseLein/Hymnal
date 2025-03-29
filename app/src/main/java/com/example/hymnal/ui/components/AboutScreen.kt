package com.example.hymnal.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen() {
    val luke_9_23 = "\"Then He said to them all, \'If anyone desires to come after Me, let him deny " +
            "himself, and take up his cross daily, and follow Me.\'\" Luke 9:23"
    val eph_5_19 = "\"speaking to one another in psalms and hymns and spiritual songs, singing and " +
            "making melody in your heart to the Lord.\" Ephesians 5:19"

    val body = arrayOf(
        "\"For behold, the darkness shall cover the earth, and deep darkness the people; " +
                "but the LORD will arise over you, and His glory will be seen upon you.\" Isaiah 60:2.",
        "We understand, as children of God, that darkness is not the final state of things. " +
                "Although we see great darkness in the world, we know that Light has come. We are " +
                "certain that a Revival is underway. So in this His day, we implore you to follow Him " +
                "in discipleship.",
        "It is our prayer that as you sing these hymns which have been sung for ages by men who " +
                "experienced personal and corporate Revival, you also will experience Revival. Amen."
    )

    LazyColumn {
        arrayOf(luke_9_23, eph_5_19).forEach { text ->
            item {
                Text(
                    text = text,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 16.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontStyle = FontStyle.Italic,
                        fontFamily = FontFamily.Serif
                    )
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        body.forEach { text ->
            item {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = FontFamily.Serif
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }

        item {
            val annotatedText = buildAnnotatedString {
                append("Music Credits: ")
                append("Pastor Abel Abikiaje and the New Covenant Baptist Church")
                addStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold
                    ),
                    start = 0,
                    end = "Music Credits:".length
                )
            }

            Text(
                text = annotatedText,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            )
        }

    }
}