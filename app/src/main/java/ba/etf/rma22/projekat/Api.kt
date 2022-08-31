package ba.etf.rma22.projekat

import ba.etf.rma22.projekat.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface Api {

    //Istrazivanja

    @GET("/istrazivanje")
    suspend fun getIstrazivanja(@Query("offset")offset: Int): List<Istrazivanje>

    @GET("/istrazivanje/{id}")
    suspend fun getIstrazivanjeById(@Path("id") id: Int): Istrazivanje

    @GET("/grupa/{gid}/istrazivanje")
    suspend fun getIstrazivanjeByGroupId(@Path("gid") gid: Int): List<Istrazivanje>

    //Grupe

    @GET("/anketa/{id}/grupa")
    suspend fun getGrupeByAnketaId(@Path("id") id: Int): List<Grupa>

    @POST("/grupa/{gid}/student/{id}")
    suspend fun upisiUGrupu(@Path("gid") idGrupe: Int, @Path("id") hashStudenta: String): Message

    @GET("/student/{id}/grupa")
    suspend fun getGrupeByStudentId(@Path("id") hashStudenta: String): List<Grupa>

    @GET("/grupa")
    suspend fun getGrupe(): List<Grupa>

    @GET("/grupa/{id}")
    suspend fun getGrupaById(): Grupa



    //Ankete

    @GET("/anketa")
    suspend fun getAnkete(@Query("offset") offset: Int): List<Anketa>

    @GET("/anketa/{id}")
    suspend fun getAnketaById(@Path("id") id: Int): Anketa

    @GET("/grupa/{id}/ankete")
    suspend fun getAnketeByGroupId(@Path("id") gid: Int): List<Anketa>

    //Odgovori

    @GET("/student/{id}/anketataken/{ktid}/odgovori")
    suspend fun getOdgovoriAnketa(@Path("id")  hashStudenta: String, @Path("ktid") ktid : Int): List<Odgovor>

    @POST("/student/{id}/anketataken/{ktid}/odgovor")
    suspend fun postaviOdgovorAnketa(@Path("id")  hashStudenta: String, @Path("ktid") ktid : Int, @Body odgovor: OdgPitanja) : Message

    //AnketaTaken

    @GET("/student/{id}/anketataken")
    suspend fun getPoceteAnkete(@Path("id") hashStudenta: String): List<AnketaTaken>

    @POST("/student/{id}/anketa/{kid}")
    suspend fun zapocniAnketu(@Path("kid") idAnkete: Int, @Path("id") hashStudenta: String): AnketaTaken



    //Account

    @GET("/student/{id}")
    suspend fun getAccount(@Path("id") hashStudenta: String): Account

    @DELETE("/student/{id}/upisugrupeipokusaji")
    suspend fun obrisiPodatke(@Path("id") idStudenta: String): String

    //Pitanje

    @GET("/anketa/{id}/pitanja")
    suspend fun getPitanja(@Path("id") idKviza: Int): List<PitanjeNovo>

}