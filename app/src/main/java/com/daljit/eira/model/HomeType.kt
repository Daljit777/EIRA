package com.daljit.eira.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.daljit.eira.ui.theme.NeonBlue
import com.daljit.eira.ui.theme.NeonGreen
import com.daljit.eira.ui.theme.NeonOrange
import com.daljit.eira.ui.theme.NeonPink

enum class HomeType(val rooms: List<Room>) {
    ONE_BHK(
        listOf(
            Room("Living Room", listOf(
                Device("Smart TV", Icons.Default.Tv, "tv_1", DeviceType.TV),
                Device("AC", Icons.Default.AcUnit, "ac_1", DeviceType.AC),
                Device("Main Light", Icons.Default.LightbulbCircle, "light_1", DeviceType.LIGHT)
            )),
            Room("Bedroom", listOf(
                Device("AC", Icons.Default.AcUnit, "ac_2", DeviceType.AC),
                Device("Light", Icons.Default.LightbulbCircle, "light_2", DeviceType.LIGHT),
                Device("Fan", Icons.Default.Air, "fan_1", DeviceType.FAN)
            )),
            Room("Kitchen", listOf(
                Device("Light", Icons.Default.LightbulbCircle, "light_3", DeviceType.LIGHT),
                Device("Refrigerator", Icons.Default.Kitchen, "fridge_1", DeviceType.APPLIANCE),
                Device("Microwave", Icons.Default.Microwave, "microwave_1", DeviceType.APPLIANCE)
            ))
        )
    ),
    TWO_BHK(
        listOf(
            Room("Living Room", listOf(
                Device("Smart TV", Icons.Default.Tv, "tv_1", DeviceType.TV),
                Device("AC", Icons.Default.AcUnit, "ac_1", DeviceType.AC),
                Device("Main Light", Icons.Default.LightbulbCircle, "light_1", DeviceType.LIGHT)
            )),
            Room("Master Bedroom", listOf(
                Device("AC", Icons.Default.AcUnit, "ac_2", DeviceType.AC),
                Device("Light", Icons.Default.LightbulbCircle, "light_2", DeviceType.LIGHT),
                Device("Fan", Icons.Default.Air, "fan_1", DeviceType.FAN)
            )),
            Room("Second Bedroom", listOf(
                Device("AC", Icons.Default.AcUnit, "ac_3", DeviceType.AC),
                Device("Light", Icons.Default.LightbulbCircle, "light_3", DeviceType.LIGHT),
                Device("Fan", Icons.Default.Air, "fan_2", DeviceType.FAN)
            )),
            Room("Kitchen", listOf(
                Device("Light", Icons.Default.LightbulbCircle, "light_4", DeviceType.LIGHT),
                Device("Refrigerator", Icons.Default.Kitchen, "fridge_1", DeviceType.APPLIANCE),
                Device("Microwave", Icons.Default.Microwave, "microwave_1", DeviceType.APPLIANCE)
            ))
        )
    ),
    THREE_BHK(
        listOf(
            Room("Living Room", listOf(
                Device("Smart TV", Icons.Default.Tv, "tv_1", DeviceType.TV),
                Device("AC", Icons.Default.AcUnit, "ac_1", DeviceType.AC),
                Device("Main Light", Icons.Default.LightbulbCircle, "light_1", DeviceType.LIGHT)
            )),
            Room("Master Bedroom", listOf(
                Device("AC", Icons.Default.AcUnit, "ac_2", DeviceType.AC),
                Device("Light", Icons.Default.LightbulbCircle, "light_2", DeviceType.LIGHT),
                Device("Fan", Icons.Default.Air, "fan_1", DeviceType.FAN)
            )),
            Room("Second Bedroom", listOf(
                Device("AC", Icons.Default.AcUnit, "ac_3", DeviceType.AC),
                Device("Light", Icons.Default.LightbulbCircle, "light_3", DeviceType.LIGHT),
                Device("Fan", Icons.Default.Air, "fan_2", DeviceType.FAN)
            )),
            Room("Third Bedroom", listOf(
                Device("AC", Icons.Default.AcUnit, "ac_4", DeviceType.AC),
                Device("Light", Icons.Default.LightbulbCircle, "light_4", DeviceType.LIGHT),
                Device("Fan", Icons.Default.Air, "fan_3", DeviceType.FAN)
            )),
            Room("Kitchen", listOf(
                Device("Light", Icons.Default.LightbulbCircle, "light_5", DeviceType.LIGHT),
                Device("Refrigerator", Icons.Default.Kitchen, "fridge_1", DeviceType.APPLIANCE),
                Device("Microwave", Icons.Default.Microwave, "microwave_1", DeviceType.APPLIANCE)
            ))
        )
    )
}

data class Room(
    val name: String,
    val devices: List<Device>
)

data class Device(
    val name: String,
    val icon: ImageVector,
    val id: String,
    val type: DeviceType,
    val color: Color = when(type) {
        DeviceType.LIGHT -> NeonGreen
        DeviceType.AC -> NeonBlue
        DeviceType.FAN -> NeonOrange
        DeviceType.TV -> NeonPink
        DeviceType.APPLIANCE -> Color.Gray
    }
)

enum class DeviceType {
    LIGHT,
    AC,
    FAN,
    TV,
    APPLIANCE
} 