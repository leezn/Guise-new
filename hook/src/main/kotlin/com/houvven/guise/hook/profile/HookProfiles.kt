package com.houvven.guise.hook.profile

import com.houvven.guise.hook.profile.item.PropertiesProfile
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json


@Serializable
data class HookProfiles(
    @Transient
    val packageName: String? = null,
    val properties: PropertiesProfile = PropertiesProfile.Empty,

    // Package Info
    val versionName: String? = null,
    val versionCode: Int? = null,

    // Resource Configuration
    val language: String? = null,
    val densityDpi: Int? = null,
    val fontScale: Float? = null,
    val nightMode: Boolean? = null,

    // Network
    val networkType: Int? = null,
    val mobileNetType: Int? = null,
    val simOperator: String? = null,

    val wifiSsid: String? = null,
    val wifiBssid: String? = null,
    val wifiMac: String? = null,

    // Base station
    val cid: Long? = null,
    val lac: Int? = null,
    val pci: Int? = null,

    // Location
    /** 经度 */
    val longitude: Double? = null,
    /** 维度 */
    val latitude: Double? = null,
    /** 返回空的WiFi信息  */
    val disableWifiLocation: Boolean = false,
    /** 返回空的基站信息 */
    val disableCellLocation: Boolean = false,


    val ssaid: String? = null,

    val timezone: String? = null,
) : Profile {

    val mcc = simOperator?.substring(0, 3)
    val mnc = simOperator?.substring(3)
    val tac = lac

    val isLocationAvailable get() = listOf(latitude, longitude).any { it != null }

    override val isAvailable: Boolean
        get() = this != Empty.copy(packageName = this.packageName)

    fun toJsonStr(): String {
        return Json.encodeToString(serializer(), this)
    }

    companion object {
        @JvmStatic
        val Empty = HookProfiles()

        @JvmStatic
        val Debug = HookProfiles(
            properties = PropertiesProfile(
                brand = "Xiaomi",
                model = "M2105K81C",
                characteristics = "tablet"
            )
        )

        fun fromJsonStr(json: String): HookProfiles {
            return runCatching { Json.decodeFromString(serializer(), json) }.getOrDefault(Empty)
        }

    }
}