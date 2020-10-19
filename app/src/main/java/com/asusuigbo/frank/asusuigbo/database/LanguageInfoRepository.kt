package com.asusuigbo.frank.asusuigbo.database

import androidx.lifecycle.LiveData
import javax.inject.Inject


//it depends on
//private val userId: String, private val dao: LanguageInfoDao --> for LanguageInfoRepository to be created
class LanguageInfoRepository @Inject constructor(private val userId: String, val dao: LanguageInfoDao) {

    suspend fun getActiveLanguage(): LanguageInfo?{
        return dao.getActiveLanguage(userId)
    }

    suspend fun addLanguage(languageInfo: LanguageInfo){
        dao.addLanguage(languageInfo)
    }

    suspend fun getLanguage(language: String): LanguageInfo?{
        return dao.getLanguage(userId, language)
    }

    fun getAllLanguages(): LiveData<List<LanguageInfo>> {
        return dao.getAllLanguages(userId)
    }

    suspend fun updateAll(notActive: Boolean){
        dao.updateAll(userId, notActive)
    }

    suspend fun updateNewActive(language: String){
        dao.updateNewActive(userId, language)
    }

    suspend fun deleteAll(){
        dao.deleteAll(userId)
    }

    suspend fun delete(language: String){
        dao.delete(userId, language)
    }
}