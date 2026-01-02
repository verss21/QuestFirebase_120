package com.example.firebase.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firebase.R
import com.example.firebase.modeldata.Siswa
import com.example.firebase.view.route.DestinasiHome
import com.example.firebase.viewmodel.HomeViewModel
import com.example.firebase.viewmodel.PenyediaViewModel
import com.example.firebase.viewmodel.StatusUiSiswa

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToItemEntry: () -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            SiswaTopAppBar(
                title = stringResource(DestinasiHome.titleRes),
                canNavigateBack = false
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.entry_siswa)
                )
            }
        }
    ) { innerPadding ->
        HomeBody(
            statusUiSiswa = viewModel.statusUiSiswa,
            onSiswaClick = navigateToItemUpdate,
            retryAction = viewModel::loadSiswa,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize() // ✅ dipindah ke sini
        )
    }
}

@Composable
fun HomeBody(
    statusUiSiswa: StatusUiSiswa,
    onSiswaClick: (Int) -> Unit,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) { // ❌ tidak pakai fillMaxSize lagi
        when (statusUiSiswa) {
            is StatusUiSiswa.Loading -> {
                LoadingScreen(modifier = Modifier.align(Alignment.Center))
            }

            is StatusUiSiswa.Success -> {
                DaftarSiswa(
                    itemSiswa = statusUiSiswa.siswa,
                    onSiswaClick = { onSiswaClick(it.id.toInt()) }
                )
            }

            is StatusUiSiswa.Error -> {
                ErrorScreen(
                    retryAction = retryAction,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun ErrorScreen(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.gagal),
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        )
        Button(onClick = retryAction) {
            Text(text = stringResource(R.string.retry))
        }
    }
}

@Composable
fun DaftarSiswa(
    itemSiswa: List<Siswa>,
    onSiswaClick: (Siswa) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            dimensionResource(id = R.dimen.padding_medium)
        ),
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.padding_small)
        )
    ) {
        items(
            items = itemSiswa,
            key = { it.id }
        ) { person ->
            ItemSiswa(
                siswa = person,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSiswaClick(person) }
            )
        }
    }
}

@Composable
fun ItemSiswa(
    siswa: Siswa,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(id = R.dimen.padding_small)
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = siswa.nama,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null
                )
                Text(
                    text = siswa.telpon,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(
                        start = dimensionResource(id = R.dimen.padding_small)
                    )
                )
            }
            Text(
                text = siswa.alamat,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
