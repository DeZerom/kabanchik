package ru.kabanchik.pro.feature.chatDetails.internal.list

import ru.kabanchik.common.store.BaseCoroutineStore
import ru.kabanchik.pro.feature.chatDetails.api.list.ProChatsListContract.Event
import ru.kabanchik.pro.feature.chatDetails.api.list.ProChatsListContract.SideEffect
import ru.kabanchik.pro.feature.chatDetails.api.list.ProChatsListContract.State

class ProChatsListStore(

) : BaseCoroutineStore<Event, State, SideEffect>() {
    init {

    }

    override fun initState(): State {
        return State()
    }

    override fun handleEvent(event: Event) {
        when (event) {
            Event.RequestChat -> TODO()
        }
    }


}