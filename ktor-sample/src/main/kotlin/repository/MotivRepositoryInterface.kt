package com.example.repository

import com.example.data.remote.tables.MotivData

interface MotivRepositoryInterface {
    fun insertMotivData(motiv: MotivData)
}