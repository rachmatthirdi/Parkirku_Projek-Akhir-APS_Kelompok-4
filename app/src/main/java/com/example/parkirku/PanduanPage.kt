package com.example.parkirku

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

@Composable
fun PanduanPage(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 20.dp)
            .padding(bottom = 120.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Image(
            painter = painterResource(id = R.drawable.parkir),
            contentDescription = "Parking Rules",
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))


        Text(
            text = "Aturan Parkir",
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color(0xFFFF6700),
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(8.dp))


        val rules = listOf(
            "Parkir kendaraan dengan posisi lurus.",
            "Parkir tidak menyisakan banyak ruang di kanan dan kiri untuk memaksimalkan tempat.",
            "Jangan memakan lebih dari satu slot parkir.",
            "Jangan tinggalkan barang berharga di dalam kendaraan.",
            "Tidak mengunci stang supaya penjaga parkir dapat merapihkan parkiran jika diperlukan.",
            "Tidak mengganggu kendaraan lain saat parkir.",
            "Parkir sesuai area yang ditentukan, dilarang parkir di area yang terdapat pembatas.",
            "Patuhi setiap arahan dari penjaga parkir jika ada."
        )

        rules.forEachIndexed { index, rule ->
            Text(
                text = "${index + 1}. $rule",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF103783)
                ),
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        Text(
            text = "Halaman ini dirancang untuk memudahkan mahasiswa dan pengguna memahami tata cara parkir yang efektif dan efisien sesuai dengan aturan sistem Parkirku.",
            fontFamily = FontFamily(Font(R.font.dm_sans_medium)),
            fontSize = 8.sp,
            color = Color.Gray,
            modifier = Modifier.fillMaxSize(),
            lineHeight = 1.5.em
        )
    }
}