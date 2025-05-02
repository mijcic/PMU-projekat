package rs.ac.bg.etf.projekat

import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.retrofit.models.AlibiData
import rs.ac.bg.etf.projekat.data.retrofit.models.DokazData
import rs.ac.bg.etf.projekat.data.retrofit.models.ForenzickiDokaz
import rs.ac.bg.etf.projekat.data.retrofit.models.MisijaData
import rs.ac.bg.etf.projekat.data.retrofit.models.MisijaPoruka
import rs.ac.bg.etf.projekat.data.retrofit.models.MotivData
import rs.ac.bg.etf.projekat.data.retrofit.models.ObdukcijaData
import rs.ac.bg.etf.projekat.data.retrofit.models.OsumnjicenData
import rs.ac.bg.etf.projekat.data.retrofit.models.SvedokData
import rs.ac.bg.etf.projekat.data.retrofit.models.Telefon
import rs.ac.bg.etf.projekat.data.retrofit.models.ZlocinRequest
import rs.ac.bg.etf.projekat.data.retrofit.models.ZrtvaData

fun InsertData(viewModel: MyViewModel){
    /*
    val osumnjicenDokaz =

        OsumnjicenData(
        idOsumnjicen =0,
        ime = "Amelia Fontaine",
        status = 0,
        tipOsumnjicen = 1,
        motiv = 3,
        kriv =1,
        odnosZrtva = "rivalski"
    )
    val osumnjicenBelini = OsumnjicenData(
        id=0,
        ime = "Marco Bellini",
        tipOsumnjicen = 1,
        motiv = 1,
        status = 0,
        kriv = 0,
        odnosZrtva = "poslovni"
    )
    val svedok1=null
    val zlocinRequest = ZlocinRequest(
        zlocin = rs.ac.bg.etf.projekat.data.retrofit.models.ZlocinData(
            id = 1,
            idTipZlocina = 1,
            naziv = "Ubistvo Isabelle Moreau",
            datum = System.currentTimeMillis(), //sadasnji datum u Long formatu
            mesto = "Hotel u Monte Karlu",
            opis = "Ubistvo poznate poslovne žene Isabelle Moreau pod misterioznim okolnostima.",
            status = "u_istrazi"
        ),
        zrtva = ZrtvaData(
            id=0,
            ime = "Isabelle Moreau",
            tipZrtve = "osoba",
            detalji = "Poznata poslovna žena sa dugovima zbog kockarske zavisnosti.",
            statusZrtva = "mrtva"
        ),
        motivi = listOf(
            MotivData(
                idMotiv = 0,
                opis = "Zrtva mu je dugovala novac zbog kockarske zavisnosti"
            ),
            MotivData(
                idMotiv = 0,
                opis = "Ljubomora zbog Isabelleine veze sa njegovom ženom"
            ),
            MotivData(
                idMotiv = 0,
                opis = "Ljubomora, zavist i želja za osvetom zbog nepriznate " +
                        "ljubavi prema Marcu i osećaja manje vrednosti pored Isabelle"
            )
        ),
        osumnjicen = listOf(
            osumnjicenBelini,
            OsumnjicenData(
                id=0,
                ime = "Vincent Duval",
                status = 0,
                tipOsumnjicen = 1,
                motiv = 2,
                kriv = 0,
                odnosZrtva = "ljubavni"
            ),
            osumnjicenDokaz
        ),
        dokazi = listOf(
            DokazData(
                id=0,
                tipDokaza = "fizicki",
                opis = "Krvavi nož sa inicijalima 'M.B.'",
                status = 1,
                osumnjicen = osumnjicenDokaz
            ),
            DokazData(
                id=0,
                tipDokaza = "digitalni",
                opis = "Isabelle je primala preteće poruke na WhatsApp-u",
                status = 1,
                osumnjicen = osumnjicenDokaz
            )
        ),
        svedok = listOf(
            SvedokData(
                id=0,
                ime = "Amelia Fontaine",
                izjava = "Tvrdila je da je videla Marca u blizini sobe žrtve.",
                kontakt = "+377 556 789",
                statusSvedok = "nesaradnja",
                status = 0
            )
        ),
        alibi = listOf(
            AlibiData(
                id=0,
                osumnjicen = osumnjicenBelini,
                svedok = SvedokData(id=0, ime = "", izjava = "", kontakt = "", statusSvedok = "", status = 0),
                opis = "Tvrdila je da je u trenutku ubistva bio u kazinu, igrajuci poker.",
                statusAlibija = "lazan"
            ),
            AlibiData(
                id=0,
                osumnjicen = osumnjicenDokaz,
                svedok = SvedokData(id=0, ime = "", izjava = "", kontakt = "", statusSvedok = "", status = 0),
                opis = "Tvrdila je da je u trenutku ubistva bila u kazinu.",
                statusAlibija = "lazan"
            )
        ),
        obdukcija = ObdukcijaData(
            id = 0,
            datum =1689177600000,
            izvestaj = "Na telu su pronađeni tragovi samoodbrane, " +
                    "a smrt je nastupila usled višestrukih ubodnih rana u predelu grudnog koša.",
            uzrokSmrti = "Višestruke ubodne rane",
            informacije = "Na noktima žrtve pronađeni su ostaci kože, ali analiza DNK još uvek traje."
        ),
        forenzickiDokazi = listOf(
            ForenzickiDokaz(
                id = 0,
                tipForenzickiDokaz = "DNK",
                opis = "Na noktima žrtve pronađeni su ostaci kože. Čeka se rezultat analize.",
                statusS = 0,
                veza = "Potencijalna povezanost sa osumnjičenim Marcom Bellinijem."
            )
        ),
        telefon = Telefon(
            id = 0,
            model = "iPhone 14 Pro",
            os="IOS",
            sifra="4862",
            informacije = "Pronađena misteriozna poruka u aplikaciji Notes: 'Kraljica srca zna istinu.'"
        ),
        misijaPoruka = listOf(
            MisijaPoruka(
                id=0,
                naziv ="Skrivena karta" ,
                statusS = 0,
                posiljalac = "Amelia Fontaine",
                poruka ="Korisniku je stigla poruka sa nepoznatog broja sa sadržajem: 'Znaš da je Marco samo pijun. Prava istina je zakopana dublje. Traži karticu kraljice srca.''"
            )
        ),
        misija = MisijaData(
            naziv = "Skrivena karta",
            opis = "Otkriveno je da je Amelia Fontaine pravi ubica.",
            status = 1
        )
    )
    viewModel.insertDataZlocin(zlocinRequest)
    */
}