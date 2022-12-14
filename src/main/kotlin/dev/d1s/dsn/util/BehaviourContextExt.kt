/*
 * Copyright 2022-2023 Mikhail Titov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.d1s.dsn.util

import dev.d1s.dsn.service.GroupChatService
import dev.inmo.tgbotapi.extensions.api.send.reply
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.extensions.utils.fromUserOrNull
import dev.inmo.tgbotapi.types.BotCommand
import dev.inmo.tgbotapi.types.chat.GroupChat
import dev.inmo.tgbotapi.types.message.content.TextMessage
import dev.inmo.tgbotapi.utils.PreviewFeature

suspend fun <BC : BehaviourContext> BC.onCommand(botCommand: BotCommand, receiver: suspend BC.(TextMessage) -> Unit) {
    onCommand(botCommand.command, scenarioReceiver = receiver)
}

suspend inline fun <BC : BehaviourContext> BC.requireGroupChat(message: TextMessage, block: BC.() -> Unit) {
    if (message.chat !is GroupChat) {
        val notGroupChatContent = makeTitle(Emoji.CROSS_MARK, "Это не групповой чат.")

        reply(message, notGroupChatContent)
    } else {
        block()
    }
}

suspend fun <BC : BehaviourContext> BC.requireInitializedGroupChatOrOwner(
    groupChatService: GroupChatService,
    message: TextMessage,
    block: suspend BC.() -> Unit
) {
    val initializedGroupChat = groupChatService.getGroupChatInfo() ?: run {
        commandNotAvailable(message)

        return
    }

    if (message.isFromOwner(groupChatService)) {
        block()

        return
    }

    val thisChatId = message.chat.id.chatId

    if (initializedGroupChat.groupChatId.chatId == thisChatId) {
        block()
    } else {
        val wrongChatContent = makeTitle(Emoji.CROSS_MARK, "Этот чат не подходит для исполнения запрашиваемой команды.")

        reply(message, wrongChatContent)
    }
}

suspend fun <BC : BehaviourContext> BC.requireOwner(
    groupChatService: GroupChatService,
    message: TextMessage,
    block: suspend BC.() -> Unit
) {
    groupChatService.getGroupChatInfo()?.ownerId ?: run {
        commandNotAvailable(message)

        return
    }

    if (message.isFromOwner(groupChatService)) {
        block()
    } else {
        val noPermissionContent = makeTitle(Emoji.CROSS_MARK, "Нет привилегий.")

        reply(message, noPermissionContent)
    }
}

@OptIn(PreviewFeature::class)
private suspend fun TextMessage.isFromOwner(
    groupChatService: GroupChatService
): Boolean {
    val thisUser = fromUserOrNull()?.user?.id?.chatId

    val ownerId = groupChatService.getGroupChatInfo()?.ownerId

    return ownerId?.chatId == thisUser
}

private suspend fun <BC : BehaviourContext> BC.commandNotAvailable(message: TextMessage) {
    val commandNotAvailableContent = makeTitle(Emoji.CROSS_MARK, "Команда еще не доступа.")

    reply(message, commandNotAvailableContent)
}