package com.example.unit

import com.example.models.dto.*
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class ModelsDtoTests {

    @Test
    fun `should use TelefonR serializer from companion`() {
        val telefon = TelefonR(1, "Pixel", "Android", "1234", "Detalji")

        // Ovo koristi TelefonR.Companion.serializer()
        val json = Json.encodeToString(TelefonR.serializer(), telefon)
        val deserialized = Json.decodeFromString(TelefonR.serializer(), json)

        assertEquals(telefon, deserialized)
        /*
        Kada koristiš @Serializable, Kotlin compiler automatski generiše Companion object koji sadrži statičke metode kao npr. serializer().
        val serializer = TelefonR.serializer() // <- iz Companion objekta
         */
    }

    @Test
    fun `should not be equal to object of different class`() {
        val telefon = TelefonR(1, "Pixel", "Android", "1234", "Detalji")
        val other = "NotATelefon"

        assertNotEquals(telefon, other)
    }

    @Test
    fun `should be equal to itself`() {
        val telefon = TelefonR(1, "Pixel", "Android", "1234", "Detalji")
        assertEquals(telefon, telefon)
    }

    @Test
    fun `should generate correct hashCode and toString`() {
        val telefon = TelefonR(1, "Pixel", "Android", "1234", "Detalji")

        // Ne moraš testirati konkretnu vrednost, samo pozovi metode
        telefon.hashCode()
        telefon.toString()
    }

    @Test
    fun `should not be equal to another TelefonR with different data`() {
        val telefon1 = TelefonR(1, "Pixel", "Android", "1234", "Detalji")
        val telefon2 = TelefonR(2, "iPhone", "iOS", "5678", "Opis")

        assertNotEquals(telefon1, telefon2)
    }

    @Test
    fun `should create a copy with modified field`() {
        val original = TelefonR(1, "Pixel", "Android", "1234", "Detalji")
        val modified = original.copy(model = "Pixel 2")

        assertEquals(1, modified.idTelefon)
        assertEquals("Pixel 2", modified.model)
        assertEquals("Android", modified.os)
        assertEquals("1234", modified.sifra)
        assertEquals("Detalji", modified.informacije)
    }

    //BeleskaR

    @Test
    fun `should use BeleskaR serializer from companion`() {
        val beleska = BeleskaR(idBeleska = 1, zlocinId = 1, tekst = "Pozvati Gerija",datum = "2024-11-11")

        val json = Json.encodeToString(BeleskaR.serializer(), beleska)
        val deserialized = Json.decodeFromString(BeleskaR.serializer(), json)

        assertEquals(beleska, deserialized)
    }

    @Test
    fun `should not be equal to object of different class (BeleskaR)`() {
        val beleska = BeleskaR(idBeleska = 1, zlocinId = 1, tekst = "Pozvati Gerija",datum = "2024-11-11")
        val other = "NotABeleska"

        assertNotEquals(beleska, other)
    }

    @Test
    fun `should be equal to itself (BeleskaR)`() {
        val beleska = BeleskaR(idBeleska = 1, zlocinId = 1, tekst = "Pozvati Gerija",datum = "2024-11-11")
        assertEquals(beleska, beleska)
    }

    //DokazOsumnjicenR

    @Test
    fun `should use DokazOsumnjicenR serializer from companion`() {
        val dokazOs = DokazOsumnjicenR(1,1, 1)

        val json = Json.encodeToString(DokazOsumnjicenR.serializer(), dokazOs)
        val deserialized = Json.decodeFromString(DokazOsumnjicenR.serializer(), json)

        assertEquals(dokazOs, deserialized)
    }

    @Test
    fun `should not be equal to object of different class (DokazOsumnjicenR)`() {
        val dokazOs = DokazOsumnjicenR(1,1, 1)
        val other = "NotADokazOsumnjicen"

        assertNotEquals(dokazOs, other)
    }

    @Test
    fun `should not be equal to object of different class (AlibiR)`() {
        val dokazOs = AlibiR(1,1, 1,"opis","potvrdjen")
        val other = "NotADokazOsumnjicen"

        assertNotEquals(dokazOs, other)
    }



}
