package ba.etf.rma22.projekat.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ba.etf.rma22.projekat.data.dao.*
import ba.etf.rma22.projekat.data.models.*

@Database(entities = [Anketa::class, Grupa::class, Istrazivanje::class, Pitanje::class, Odgovor::class, Account::class, AnketaTakenNovo::class, UpisanaAnketa::class, PitanjeBaza::class], version = 3)
abstract class AppDatabase: RoomDatabase()  {
    abstract fun anketaDao(): AnketaDao
    abstract fun istrazivanjeDao(): IstrazivanjeDao
    abstract fun grupaDao(): GrupaDao
    abstract fun pitanjeDao(): PitanjeDao
    abstract fun odgovorDao(): OdgovorDao
    abstract fun accountDao(): AccountDao
    abstract fun anketaTakenDao(): AnketaTakenDao
    abstract fun upisanaAnketaDao(): UpisanaAnketaDao
    abstract fun pitanjeBazaDao(): PitanjeBazaDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = buildRoomDB(context)
                }
            }
            return INSTANCE!!
        }
        private fun buildRoomDB(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "RMA22DB"
            ).allowMainThreadQueries()
                .build()
    }



}