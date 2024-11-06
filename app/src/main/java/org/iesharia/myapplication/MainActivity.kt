package org.iesharia.myapplication

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.iesharia.myapplication.ui.theme.MyApplicationTheme

/*
CRUD
Create
Read
Update
Delete
*/

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) { innerPadding ->
                    MainActivity (
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun MainActivity(modifier: Modifier) {
    val context = LocalContext.current
    val db = DBHelper(context)

    var lName:String by remember { mutableStateOf("") }
    var lAge:String by remember { mutableStateOf("") }

    Column (
        verticalArrangement = Arrangement.Center,
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Base de Datos",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp
        )
        Text(
            text = "NO Muuuuuy simple\nNombre/Edad",
            fontSize = 10.sp

        )
        //Nombre
        var nameValue by remember { mutableStateOf("") }
        OutlinedTextField(
            value = nameValue,
            onValueChange = {
                nameValue = it
            },
            modifier = Modifier,
            textStyle = TextStyle(color = Color.DarkGray),
            label = { Text(text = "Nombre") },
            singleLine = true,
            shape = RoundedCornerShape(10.dp)
        )
        //Edad
        var ageValue by remember { mutableStateOf("") }
        OutlinedTextField(
            value = ageValue,
            onValueChange = {
                ageValue = it
            },
            modifier = Modifier,
            textStyle = TextStyle(color = Color.DarkGray),
            label = { Text(text = "Edad") },
            singleLine = true,
            shape = RoundedCornerShape(10.dp)
        )
        val bModifier:Modifier = Modifier.padding(20.dp)
        // El <Triple> es para identificar cada string y agruparlo como 1 objeto
        var peopleList by remember { mutableStateOf(listOf<Triple<Int, String, String>>()) }

        Row {
            Button(
                modifier = bModifier,
                onClick = {
                    val name = nameValue
                    val age = ageValue

                    db.addName(name, age)

                    Toast.makeText(
                        context,
                        name + " adjuntado a la base de datos",
                        Toast.LENGTH_LONG)
                        .show()

                    nameValue = ""
                    ageValue = ""
                }
            ) {
                Text(text = "Añadir")
            }
            Button(
                modifier = bModifier,
                onClick = {
                    peopleList = emptyList()
                    val db = DBHelper(context, null)
                    val cursor = db.getName()

                    // Recorrer el cursor y añadir cada nombre y edad a la lista de personas
                    val tempList = mutableListOf<Triple<Int, String, String>>()
                    cursor?.let {
                        if (cursor.moveToFirst()) {
                            do {
                                val id = cursor.getInt(cursor.getColumnIndex(DBHelper.ID_COL))
                                val name = cursor.getString(cursor.getColumnIndex(DBHelper.NAME_COl))
                                val age = cursor.getString(cursor.getColumnIndex(DBHelper.AGE_COL))
                                tempList.add(Triple(id, name, age))
                            } while (cursor.moveToNext())
                        }
                        cursor.close()
                    }

                    peopleList = tempList
                }
            ) {
                Text(text = "Mostrar")
            }
        }
        Row {
            Text(
                modifier = Modifier.padding(10.dp),
                text = "Nombre"
            )
            Text(
                modifier = Modifier.padding(10.dp),
                text = "Edad"
            )
        }
        Column {
            peopleList.forEach { person ->
                Row(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(15.dp),
                        text = person.second
                    )
                    Text(
                        modifier = Modifier.padding(15.dp),
                        text = person.third
                    )
                    Button(
                        modifier = Modifier,
                        onClick = {

                        }
                    ) {
                        Text(text = "✍")
                    }
                    Button(
                        modifier = Modifier,
                        onClick = {
                            val db = DBHelper(context, null)
                            db.deleteUser(person.first)

                            // Actualiza la lista
                            peopleList = peopleList.filter { it.first != person.first }
                        }
                    ) {
                        Text(text = "╳")
                    }
                }
            }
        }
    }
}
