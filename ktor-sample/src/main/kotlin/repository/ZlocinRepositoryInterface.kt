package com.example.repository

import com.example.data.remote.tables.UsedZlocinData
import com.example.data.remote.tables.ZlocinData

interface ZlocinRepositoryInterface {
    fun insertUsedZlocinData(usedZlocin: UsedZlocinData)

    fun insertZlocinData(zlocin: ZlocinData)
}