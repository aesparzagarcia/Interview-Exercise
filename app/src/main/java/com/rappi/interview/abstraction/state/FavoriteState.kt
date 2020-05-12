package com.rappi.interview.abstraction.state

sealed class FavoriteState {
    object InsertSuccess : FavoriteState()
    object DeleteSuccess : FavoriteState()
}