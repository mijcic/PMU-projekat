package com.example.models.domain

import com.example.data.remote.tables.DokazData
import com.example.data.remote.tables.ForenzickiDokazData
import com.example.data.remote.tables.TelefonData
import com.example.data.remote.tables.ZrtvaData

/**
 * Holds all types of evidence associated with a specific victim.
 *
 * @property dokaziLista A list of general evidence items related to the victim.
 * @property telefoniLista A list of phones linked to the victim.
 * @property forenzickiDokaziLista A list of forensic evidence items.
 * @property zrtva Information about the victim, or `null` if unavailable.
 */
data class SviDokaziOdZrtve(
    val dokaziLista: MutableList<DokazData>,
    val telefoniLista: MutableList<TelefonData>,
    val forenzickiDokaziLista: MutableList<ForenzickiDokazData>,
    val zrtva: ZrtvaData?
)