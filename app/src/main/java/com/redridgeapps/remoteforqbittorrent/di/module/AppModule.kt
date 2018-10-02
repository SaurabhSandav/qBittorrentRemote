package com.redridgeapps.remoteforqbittorrent.di.module

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Resources
import android.preference.PreferenceManager
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import com.redridgeapps.remoteforqbittorrent.api.QBittorrentService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
object AppModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideResources(app: Application): Resources = app.resources

    @JvmStatic
    @Provides
    @Singleton
    fun provideSharedPreferences(app: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(app)
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideChuckInterceptor(app: Application): ChuckInterceptor = ChuckInterceptor(app)

    @JvmStatic
    @Provides
    @Singleton
    fun provideCoroutineCallAdapterFactory(): CoroutineCallAdapterFactory {
        return CoroutineCallAdapterFactory()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideScalarsConverterFactory(): ScalarsConverterFactory {
        return ScalarsConverterFactory.create()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory {
        return MoshiConverterFactory.create(moshi)
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideOkHttpClient(
            chuckInterceptor: ChuckInterceptor
    ): OkHttpClient {
        return OkHttpClient()
                .newBuilder()
                .addInterceptor(chuckInterceptor)
                .build()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideRetrofit(
            okHttpClient: OkHttpClient,
            scalarsCon: ScalarsConverterFactory,
            moshiCon: MoshiConverterFactory,
            coroutineCallAdapter: CoroutineCallAdapterFactory
    ): Retrofit {
        return Retrofit.Builder()
                .baseUrl(QBittorrentService.DEFAULT_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(scalarsCon)
                .addConverterFactory(moshiCon)
                .addCallAdapterFactory(coroutineCallAdapter)
                .build()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideQBittorrentWebService(retrofit: Retrofit): QBittorrentService {
        return retrofit.create(QBittorrentService::class.java)
    }
}
