/*
 * Copyright (c) 2021 Toast Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.toasttab.pulseman.state

import androidx.compose.runtime.mutableStateOf
import com.toasttab.pulseman.entities.ButtonState
import com.toasttab.pulseman.entities.SingleSelection
import com.toasttab.pulseman.entities.TabValues
import com.toasttab.pulseman.pulsar.handlers.PulsarMessage
import com.toasttab.pulseman.thirdparty.rsyntaxtextarea.RSyntaxTextArea
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rsyntaxtextarea.Theme
import org.fife.ui.rtextarea.RTextScrollPane

class SendMessage(
    val setUserFeedback: (String) -> Unit,
    val selectedClass: SingleSelection<PulsarMessage>,
    val pulsarSettings: PulsarSettings,
    onChange: () -> Unit,
    initialSettings: TabValues? = null,
) {
    val generateState = mutableStateOf(ButtonState.WAITING)
    val sendState = mutableStateOf(ButtonState.WAITING)
    val compileState = mutableStateOf(ButtonState.WAITING)

    var generatedBytes: ByteArray? = null

    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    val textArea =
        RSyntaxTextArea.textArea(
            initialSettings?.code ?: "",
            SyntaxConstants.SYNTAX_STYLE_JAVA,
            onChange
        )

    fun close() {
        scope.cancel("Close SendMessage")
    }

    init {
        Theme.load(
            javaClass.getResourceAsStream(
                "/org/fife/ui/rsyntaxtextarea/themes/dark.xml"
            )
        ).apply { apply(textArea) }
    }

    val sp = RTextScrollPane(textArea)

    fun currentCode(): String = textArea.text
}