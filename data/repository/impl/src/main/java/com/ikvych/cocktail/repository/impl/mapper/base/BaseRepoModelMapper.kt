package com.ikvych.cocktail.repository.impl.mapper.base

abstract class BaseRepoModelMapper<RepoModel, DbModel, NetModel> {

    open fun mapDbToRepo(db: DbModel): RepoModel {
        throw NotImplementedError("provide mapping for model")
    }

    open fun mapRepoToDb(repo: RepoModel): DbModel {
        throw NotImplementedError("provide mapping for model")
    }

    open fun mapNetToRepo(net: NetModel): RepoModel {
        throw NotImplementedError("provide mapping for model")
    }

    open fun mapRepoToNet(repo: RepoModel): NetModel {
        throw NotImplementedError("provide mapping for model")
    }

    open fun mapDbToRepoList(db: List<DbModel>): List<RepoModel> = db.map(::mapDbToRepo)
    open fun mapRepoToDbList(repo: List<RepoModel>): List<DbModel> = repo.map(::mapRepoToDb)

    open fun mapNetToRepoList(net: List<NetModel>): List<RepoModel> = net.map(::mapNetToRepo)
    open fun mapRepoToNetList(repo: List<RepoModel>): List<NetModel> = repo.map(::mapRepoToNet)
}