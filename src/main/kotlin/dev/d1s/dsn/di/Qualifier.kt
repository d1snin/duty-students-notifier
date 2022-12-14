/*
 * Copyright 2022-2023 Mikhail Titov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.d1s.dsn.di

import org.koin.core.qualifier.named

object Qualifier {

    val AnnounceDutyPairJob = named(dev.d1s.dsn.job.AnnounceDutyPairJob.IDENTITY)

    val GetCurrentDutyPairCommand = named("get-current-duty-pair-command")
    val GetDutyPairsCommand = named("get-duty-pairs-command")
    val InitGroupChatCommand = named("init-group-chat-command")
    val PostponeDutyPairCommand = named("postpone-duty-pair-command")
    val ResetGroupChatCommand = named("reset-group-chat-command")
    val SetDutyPairCommand = named("set-duty-pair-command")
    val SwitchDutyPairCommand = named("switch-duty-pair-command")
    val ToggleSchedulingCommand = named("toggle-scheduling-command")
}