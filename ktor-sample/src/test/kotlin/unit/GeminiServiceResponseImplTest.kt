package com.example.unit

import com.example.getDatabaseConnection
import com.example.models.dto.gemini.*
import com.example.repository.RepositoryInsert
import com.example.service.post.GeminiServiceResponseImpl
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.*
import java.sql.Connection
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GeminiServiceResponseImplTest {

    private lateinit var conn: Connection
    private lateinit var repo: RepositoryInsert

    @BeforeAll
    fun setUpStaticMocks() {
        // PRAVA statička funkcija, ako je getDatabaseConnection u com.example
        mockkStatic("com.example.RoutingKt")
    }

    @BeforeEach
    fun setup() {
        conn = mockk(relaxed = true)
        repo = mockk(relaxed = true)

        every { getDatabaseConnection() } returns conn

        mockkConstructor(RepositoryInsert::class)
        every { anyConstructed<RepositoryInsert>().insertZlocinData(any()) } just Runs
    }

    @AfterEach
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `should parse JSON and call insertZlocinData`() = runTest {
        val jsonText = """

        {
            "zlocinR": {
                "idZlocin": 1,
                "tipZlocinaId": 1,
                "naziv": "Murder of Isabelle Moreau",
                "datum": "2025-04-17",
                "mesto": "Casino Hotel, Paris",
                "opis": "Isabelle Moreau, a high-profile gambler, was found dead in her hotel room with a knife wound. The investigation is ongoing.",
                "status": "u_istrazi"
              },
              "zrtvaR": {
                "idZrtva": 1,
                "tipZrtve": "Individual",
                "detalji": "Isabelle Moreau, a 32-year-old gambler known for her luxurious lifestyle and turbulent relationships, was found murdered in her hotel room.",
                "statusZrtva": "ziva",
                "zlocinId": 1,
                "osobaId": {
                  "idOsoba": 1,
                  "ime": "Isabelle Moreau",
                  "kontakt": "+33612345678",
                  "datum": "1993-04-12",
                  "zanimanje": "Gambler",
                  "pol": "zenski",
                  "zlocinId": 1
                }
              },
              "osumnjicenR": [
                {
                  "idOsumnjicen": 1,
                  "status": 0,
                  "tipOsumnjicen": "Pojedinac",
                  "motiv": {
                    "idMotiv": 1,
                    "opis": "Financial struggles and jealousy."
                  },
                  "zlocinId": 1,
                  "kriv": 0,
                  "osobaId": {
                    "idOsoba": 2,
                    "ime": "Amelia Fontaine",
                    "kontakt": "+33623456789",
                    "datum": "1990-06-14",
                    "zanimanje": "Casino Dealer",
                    "pol": "zenski",
                    "zlocinId": 1
                  }
                },
                {
                  "idOsumnjicen": 2,
                  "status": 0,
                  "tipOsumnjicen": "Pojedinac",
                  "motiv": {
                    "idMotiv": 2,
                    "opis": "Financial problems linked to Isabelle's gambling habits."
                  },
                  "zlocinId": 1,
                  "kriv": 0,
                  "osobaId": {
                    "idOsoba": 3,
                    "ime": "Marco Bellini",
                    "kontakt": "+33698765432",
                    "datum": "1985-02-21",
                    "zanimanje": "Gambler",
                    "pol": "muski",
                    "zlocinId": 1
                  }
                }
              ],
              "dokazR": [
                {
                  "idDokaz": 1,
                  "tipDokaza": "fizicki",
                  "opis": "A knife with blood traces found near the victim's room.",
                  "zlocinId": 1,
                  "zrtvaId": 1,
                  "status": 0
                },
                {
                  "idDokaz": 2,
                  "tipDokaza": "digitalni",
                  "opis": "Threatening messages found on Isabelle's phone.",
                  "zlocinId": 1,
                  "zrtvaId": 1,
                  "status": 0
                }
              ],
              "svedokR": [
                {
                  "idSvedok": 1,
                  "izjava": "Amelia Fontaine was seen leaving Isabelle's room shortly before the body was discovered. She seemed anxious.",
                  "statusSvedok": "aktivno",
                  "statusIspitan": 0,
                  "zlocinId": 1,
                  "osobaId": {
                    "idOsoba": 4,
                    "ime": "Luc Moreau",
                    "kontakt": "+33622334455",
                    "datum": "1989-08-05",
                    "zanimanje": "Hotel Staff",
                    "pol": "muski",
                    "zlocinId": 1
                  }
                },
                {
                  "idSvedok": 2,
                  "izjava": "I overheard a heated argument between Isabelle and Marco, but I couldn't understand what was being said.",
                  "statusSvedok": "aktivno",
                  "statusIspitan": 0,
                  "zlocinId": 1,
                  "osobaId": {
                    "idOsoba": 5,
                    "ime": "Vincent Duval",
                    "kontakt": "+33644455566",
                    "datum": "1987-12-01",
                    "zanimanje": "Casino Manager",
                    "pol": "muski",
                    "zlocinId": 1
                  }
                }
              ],
              "obdukcijaR": {
                "idObdukcija": 1,
                "izvestaj": "The victim died from a single stab wound to the chest. There was also evidence of struggle before her death.",
                "datum": "2025-04-17",
                "uzrokSmrti": "Stab wound to the chest.",
                "zrtvaId": 1,
                "informacije": "No signs of sexual assault. The victim's hands showed defensive wounds."
              },
              "forenzickiDokazR": [
                {
                  "idForenzickiDokaz": 1,
                  "tipForenzickiDokaz": "DNK",
                  "opis": "DNA traces found on the knife match those of Amelia Fontaine.",
                  "statusS": 0,
                  "veza": "The evidence strongly links Amelia Fontaine to the murder."
                }
              ],
              "telefonR": [
                {
                  "idTelefon": 1,
                  "model": "iPhone 12",
                  "os": "IOS",
                  "sifra": "123456",
                  "informacije": "The phone showed messages between the victim and the suspects. Some were threatening in nature."
                },
                {
                  "idTelefon": 2,
                  "model": "Samsung Galaxy S20",
                  "os": "Android",
                  "sifra": "654321",
                  "informacije": "The phone had records of Marco Bellini's calls with Isabelle the day before her death."
                }
              ],
              "oneContactR": [
                {
                  "idOneContact": 1,
                  "zlocinId": 1,
                  "ime": "Marco Bellini",
                  "broj": "+33698765432",
                  "slika": 1
                },
                {
                  "idOneContact": 2,
                  "zlocinId": 1,
                  "ime": "Amelia Fontaine",
                  "broj": "+33623456789",
                  "slika": 1
                }
              ],
              "beleskaR": [
                {
                  "idBeleska": 1,
                  "zlocinId": 1,
                  "tekst": "Witnesses reported seeing Amelia Fontaine near the scene of the crime.",
                  "datum": "2025-04-17"
                },
                {
                  "idBeleska": 2,
                  "zlocinId": 1,
                  "tekst": "Security footage showed Marco Bellini near Isabelle's room earlier that evening.",
                  "datum": "2025-04-17"
                }
              ],
              "whatsAppKontaktR": [
              {
                "idWhatsAppKontakt": 1,
                "zlocinId": 1,
                "ime": "Oliver Chase",
                "broj": "+12065559900",
                "slika": 1
              },
              {
                "idWhatsAppKontakt": 2,
                "zlocinId": 1,
                "ime": "Sophia Blake",
                "broj": "+12067771122",
                "slika": 1
              }],
              "whatsAppPorukaR": [
                  {
                    "idWhatsAppPoruka": 1,
                    "kontaktKoSalje": 1,
                    "kontaktKomeSalje": 2,
                    "tekst": "Nathan was getting too close. We had to act.",
                    "datum": "2025-04-17",
                    "procitana": true
                  },
                  {
                    "idWhatsAppPoruka": 2,
                    "kontaktKoSalje": 2,
                    "kontaktKomeSalje": 1,
                    "tekst": "I hope nobody traces this back to us.",
                    "datum": "2025-04-17",
                    "procitana": false
                  }
                ],
                "oneCallR": [
                {
                  "idOneCall": 1,
                  "kontakt": 1,
                  "datum": "2025-04-17",
                  "propusten": false,
                  "dolazni": true
                },
                {
                  "idOneCall": 2,
                  "kontakt": 2,
                  "datum": "2025-04-17",
                  "propusten": true,
                  "dolazni": false
                }
              ],
              "galleryR": [
              {
                "idPhoto": 1,
                "zlocinId": 1,
                "slika": 1,
                "datum": "2025-04-17",
                "mesto": "Casino Hotel, Paris"
              },
              {
                "idPhoto": 2,
                "zlocinId": 1,
                "slika": 2,
                "datum": "2025-04-17",
                "mesto": "Casino Hotel Lobby, Paris"
              }
            ],
            "obicnaPorukaR": [
                {
                  "idObicnaPoruka": 1,
                  "kontaktKoSalje": 1,
                  "kontaktKomeSalje": 2,
                  "tekst": "Videli su me u hotelu. Sta da radim?",
                  "datum": "2025-04-17",
                  "procitana": true
                },
                {
                  "idObicnaPoruka": 2,
                  "kontaktKoSalje": 2,
                  "kontaktKomeSalje": 1,
                  "tekst": "Samo se pravi da ništa ne znaš. Sve će biti u redu.",
                  "datum": "2025-04-17",
                  "procitana": false
                }
              ],
              "prijavljeniKorisnikR": [
              {
                "idKorisnik": 1,
                "korisnickoIme": "detektiv.paris",
                "sifra": "securePassword123"
              },
              {
                "idKorisnik": 2,
                "korisnickoIme": "inspektor.moreau",
                "sifra": "investigate456"
              },
              {
                "idKorisnik": 3,
                "korisnickoIme": "analiticar.bellini",
                "sifra": "analyze789"
              }
            ],
            "pitanjeR": [
              {
                "idPitanje": 1,
                "zlocinId": 1,
                "tekst": "Ko je poslednji put viđen sa Isabelle Moreau pre njene smrti?"
              },
              {
                "idPitanje": 2,
                "zlocinId": 1,
                "tekst": "Da li su pronađeni tragovi borbe u hotelskoj sobi?"
              },
              {
                "idPitanje": 3,
                "zlocinId": 1,
                "tekst": "Koji su motivi osumnjičenih Amelije Fontaine i Marca Bellinija?"
              }
            ],
            "odnosOsumnjicenZrtvaR": [
              {
                "idOdnos": 1,
                "osumnjicenId": 1,
                "zrtvaId": 1,
                "tipOdnosa": "koleginice sa posla"
              },
              {
                "idOdnos": 2,
                "osumnjicenId": 2,
                "zrtvaId": 1,
                "tipOdnosa": "kockarski rivali"
              }
            ],
            "odgovorR": [
              {
                "idOdogovor": 1,
                "pitanjeId": 1,
                "tekstOdgovora": "Amelia Fontaine je bila viđena kako izlazi iz sobe žrtve.",
                "tacan": true,
                "bodovi": 10
              },
              {
                "idOdogovor": 2,
                "pitanjeId": 1,
                "tekstOdgovora": "Marco Bellini je bio na drugom kraju grada.",
                "tacan": false,
                "bodovi": 0
              },
              {
                "idOdogovor": 3,
                "pitanjeId": 1,
                "tekstOdgovora": "Niko nije viđen u blizini sobe žrtve.",
                "tacan": false,
                "bodovi": 0
              }
            ],
            "pitanjeIspitivanjeOsumnjicenogR": [
              {
                "idPitanjeIspitivanjeOsumnjicenog": 1,
                "kategorija": "Alibi",
                "tekst": "Gde ste bili u noći kada je Nathan Clarke ubijen?",
                "odgovor": "Bio sam kod kuće, sam, gledajući TV.",
                "komentar": "Nema potvrde alibija od treće strane.",
                "osumnjicenId": 2
              },
              {
                "idPitanjeIspitivanjeOsumnjicenog": 2,
                "kategorija": "Motiv",
                "tekst": "Da li ste imali neki razlog da naudite Nathanu?",
                "odgovor": "Ne, nismo imali nikakve probleme.",
                "komentar": "Svedoci tvrde da su imali žestoku raspravu nedelju dana ranije.",
                "osumnjicenId": 2
              },
              {
                "idPitanjeIspitivanjeOsumnjicenog": 3,
                "kategorija": "Pristup mestu zločina",
                "tekst": "Da li imate ključ ili način da uđete u Nathanuov stan?",
                "odgovor": "Ne, nikada nisam imao ključ.",
                "komentar": "Forenzičari nisu pronašli tragove provale.",
                "osumnjicenId": 1
              }],
              "pitanjeIspitivanjeSvedokaR": [
                {
                  "idPitanjeIspitivanjeSvedoka": 1,
                  "tekst": "Gde ste bili u trenutku kada je zločin izveden?",
                  "odgovor": "Bio sam kod kuće.",
                  "svedokId": 2,
                  "nextPitanje": 3
                },
                {
                  "idPitanjeIspitivanjeSvedoka": 2,
                  "tekst": "Da li ste ikada imali konflikata sa osumnjičenim?",
                  "odgovor": "Ne, nikada.",
                  "svedokId": 2,
                  "nextPitanje": 0
                },
                {
                  "idPitanjeIspitivanjeSvedoka": 3,
                  "tekst": "Da li možete potvrditi alibi osumnjičenog?",
                  "odgovor": "Da, bio je sa mnom.",
                  "svedokId": 3,
                  "nextPitanje": 0
                }
              ],
              "osobaR": [
                {
                  "idOsoba": 1,
                  "ime": "Marko Marković",
                  "kontakt": "123456789",
                  "datum": "2025-04-17",
                  "zanimanje": "Detektiv",
                  "pol": "Muški",
                  "zlocinId": 101
                },
                {
                  "idOsoba": 2,
                  "ime": "Jovana Jovanović",
                  "kontakt": "987654321",
                  "datum": "2025-04-17",
                  "zanimanje": "Advokat",
                  "pol": "Ženski",
                  "zlocinId": 102
                },
                {
                  "idOsoba": 3,
                  "ime": "Nikola Nikolić",
                  "kontakt": "1122334455",
                  "datum": "2025-04-17",
                  "zanimanje": "Novinar",
                  "pol": "Muški",
                  "zlocinId": 103
                }
              ],
              "zadatakR": [
              {
                "idZadatak": 1,
                "tekst": "Ispitati mesto zločina",
                "korak": "1",
                "uradjen": false,
                "nextZadatak": 2,
                "zlocinId": 101
              },
              {
                "idZadatak": 2,
                "tekst": "Pronaći svedoke",
                "korak": "2",
                "uradjen": false,
                "nextZadatak": 3,
                "zlocinId": 101
              }
            ],
            "ispitivanjeSvedokaZadatakR":[
              {
                "idIspitivanjeSvedokaZadatak": 1,
                "svedokId": 101,
                "zadatakId": 1001,
                "uradjen": false
              },
              {
                "idIspitivanjeSvedokaZadatak": 2,
                "svedokId": 102,
                "zadatakId": 1002,
                "uradjen": true
              },
              {
                "idIspitivanjeSvedokaZadatak": 3,
                "svedokId": 103,
                "zadatakId": 1003,
                "uradjen": false
              }
            ],
            "dokazZadatakR": [
              {
                "idDokazZadatak": 1,
                "tekst": "Analiziraj DNK tragove pronađene na nožu.",
                "dokazId": 1,
                "uradjen": false,
                "zadatakId": 2
              },
              {
                "idDokazZadatak": 2,
                "tekst": "Uporedi otiske prstiju sa čaše sa bazom osumnjičenih.",
                "dokazId": 2,
                "uradjen": false,
                "zadatakId": 3
              }],
              "ispitivanjeOsumnjicenogZadatakR":[
              {
                "idIspitivanjeOsumnjicenogZadatak": 1,
                "osumnjicenId": 42,
                "zadatakId": 7,
                "uradjen": false
              },
              {
                "idIspitivanjeOsumnjicenogZadatak": 2,
                "osumnjicenId": 43,
                "zadatakId": 8,
                "uradjen": true
              },
              {
                "idIspitivanjeOsumnjicenogZadatak": 3,
                "osumnjicenId": 42,
                "zadatakId": 9,
                "uradjen": false
              }
            ],
            "telefonZadatakR": [
                  {
                    "idTelefonZadatak": 1,
                    "telefonId": 10,
                    "zadatakId": 3,
                    "uradjen": false
                  },
                  {
                    "idTelefonZadatak": 2,
                    "telefonId": 11,
                    "zadatakId": 4,
                    "uradjen": true
                  }
            ],
            "forenzickiDokazZadatakR": [
              {
                "idForenzickiDokazZadatak": 1,
                "tekst": "Uporedi DNK tragove sa uzorcima osumnjičenih.",
                "forenzickiDokazId": 1,
                "uradjen": false,
                "zadatakId": 1
              },
              {
                "idForenzickiDokazZadatak": 2,
                "tekst": "Proveri da li postoji još tragova DNK na dršci noža.",
                "forenzickiDokazId": 1,
                "uradjen": false,
                "zadatakId": 2
              }
            ],
                "kontaktKtor":[{
                    "idKontakt":0,
                    "ime":"",
                    "broj":"",
                    "status":0,
                    "zrtvaId":0
                }],
                "porukeKtor":[{
                    "idPoruke":0,
                    "tipPoruke":"",
                    "sadrzaj":"",
                    "datumVreme":"2023-11-11 8:30AM",
                    "zrtvaId":0,
                    "posiljalacId":0,
                    "statusPoruke":"",
                    "sirovana":false
                }],
                "poziviKtor":[{
                    "idPoziv":0,
                    "tip":0,
                    "broj":"",
                    "datumVreme":"2023-11-11 9:30AM",
                    "zrtvaId":0,
                    "status":0,
                    "kontaktId":0
                }],
                "galerijaKtor":[{
                    "idGalerija":0,
                    "tip":0,
                    "putanja":"",
                    "zrtvaId":0,
                    "datumVreme":"2023-11-11 9:30AM",
                    "lokacija":""
                }],
                "aplikacijaKtor":[{
                    "idAplikacije":0,
                    "naziv": "",
                    "tip": 0,
                    "zrtvaId": 0,
                    "aktivna": false,
                    "informacije": ""
                }],
                "tragKtor":[{
                    "idTrag":0,
                    "forenzickiDokazId":0,
                    "osumnjicenId":0
                }],
                "dokazOsumnjicenKtor":[{
                    "idDokazOsumnjicen":0,
                    "dokazId":0,
                    "osumnjicenId":0
                }]
        }
        """.trimIndent()

        val geminiResponse = GeminiResponse(
            candidates = listOf(
                Candidate(
                    content = Content(
                        parts = listOf(Part(text = jsonText))
                    )
                )
            )
        )

         val service = GeminiServiceResponseImpl(geminiResponse)

        val result = service.getDataGeminiResponse(geminiResponse)

        assertNotNull(result.zlocinRetrofit)
        assertEquals("Murder of Isabelle Moreau", result.zlocinRetrofit?.naziv)
        assertEquals("Casino Hotel, Paris", result.zlocinRetrofit?.mesto)
        assertEquals("Isabelle Moreau, a high-profile gambler, was found dead in her hotel room with a knife wound. The investigation is ongoing.", result.zlocinRetrofit?.opis)
        assertEquals("u_istrazi", result.zlocinRetrofit?.status)

        //verify(exactly = 1) { anyConstructed<RepositoryInsert>().insertZlocinData(any()) }
    }
}