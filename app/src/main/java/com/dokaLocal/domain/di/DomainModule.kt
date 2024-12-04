package com.dokaLocal.domain.di

import com.dokaLocal.data.repository.RepositoryImpl
import com.dokaLocal.domain.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
abstract class DomainModule {

    @Binds
    @ViewModelScoped
    abstract fun bindRepository(repository: RepositoryImpl): Repository
}