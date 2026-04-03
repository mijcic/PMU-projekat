package com.example.repository

import com.example.data.remote.tables.MotivData
import com.example.data.remote.tables.OsobaData
import com.example.data.remote.tables.OsumnjicenData
import com.example.data.remote.tables.SvedokData
import com.example.data.remote.tables.ZlocinData
import com.example.data.remote.tables.ZrtvaData

interface OsobaRepositoryInterface {
    fun insertOsobaData(osobaData: OsobaData, zlocin: ZlocinData)

    fun insertZrtva(zrtvaData: ZrtvaData, zlocin: ZlocinData, osoba: OsobaData)

    fun insertOsumnjicenData(osumnjicen: OsumnjicenData, zlocin: ZlocinData, motiv: MotivData)

    fun insertSvedokData(svedok: SvedokData, zlocin: ZlocinData)
}