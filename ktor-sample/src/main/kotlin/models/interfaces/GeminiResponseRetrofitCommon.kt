package com.example.models.interfaces

import com.example.models.dto.*

/**
 * Common interface defining shared data structure fields used in Gemini response parsing.
 *
 * This interface encapsulates various domain-specific lists of data models
 * that represent structured and pre-processed information extracted from a Gemini API response.
 */
interface GeminiResponseRetrofitCommon{
    /** A list of person-related data. */
    var osobeRetrofit: List<OsobaData>?

    /** A list of evidence-related data. */
    var dokaziRetrofit:List<DokazData>?

    /** A list of forensic evidence data. */
    var forenzickiDokazRetrofit: List<ForenzickiDokazData>?

    /** A list of tasks associated with forensic evidence. */
    var forenzickiDokazZadaciRetrofit: List<ForenzickiDokazZadatakData>?

    /** A list of phone data. */
    var telefoniRetrofit: List<TelefonData>?

    /** A list of tasks associated with phones. */
    var telefonZadaciRetrofit: List<TelefonZadatakData>?

    /** A list of mobile application data. */
    var aplikacijeRetrofit: List<AplikacijaData>?

    /** A list of one-to-one contact data. */
    var oneContactRetrofit: List<OneContactData>?

    /** A list of notes or memos. */
    var beleskeRetrofit: List<BeleskaData>?

    /** A list of WhatsApp contact data. */
    var whatsappKontaktRetrofit: List<WhatsAppKontaktData>?

    /** A list of WhatsApp messages. */
    var whatsappPorukaRetrofit: List<WhatsAppPorukaData>?

    /** A list of individual call records. */
    var oneCallRetrofit: List<OneCallData>?

    /** A list of gallery items. */
    var galleryRetrofit: List<GalleryData>?

    /** A list of standard text messages. */
    var obicnePorukeRetrofit: List<ObicnaPorukaData>?

    /** A list of questions related to the case or investigation. */
    var pitanjaRetrofit: List<PitanjeData>?

    /** A list of answers related to the questions. */
    var odgovoriRetrofit: List<OdgovorData>?

    /** A list of investigation tasks. */
    var zadaciRetrofit: List<ZadatakData>?

    /** A list of evidence items linked to tasks. */
    var dokaziZadaciRetrofit: List<DokazZadatakData>?
}