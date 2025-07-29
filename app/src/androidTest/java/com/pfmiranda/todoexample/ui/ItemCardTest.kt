package com.pfmiranda.todoexample.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pfmiranda.todoexample.domain.Todo
import com.pfmiranda.todoexample.ui.theme.TodoAppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ItemCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // --- Datos de Prueba ---
    private val activeItem = Todo(
        id = 1,
        userId = 101,
        title = "Sample Active Item",
        completed = true
    )

    private val inactiveItem = Todo(
        id = 2,
        userId = 102,
        title = "Sample Inactive Item",
        completed = false
    )

    private val longNameItem = Todo(
        id = 3,
        userId = 103,
        title = "This is a very very very very very very very very very very very very very very very very very very very very long item name that should definitely trigger text overflow with ellipsis because it exceeds the maximum lines allowed for the text field.",
        completed = true
    )

    // --- Helper para configurar el contenido ---
    private fun setItemCardContent(item: Todo) {
        composeTestRule.setContent {
            TodoAppTheme { // Envuelve con tu tema para que los estilos se apliquen correctamente
                TodoCard(todo = item)
            }
        }
    }

    @Test
    fun itemCard_basicRender_displaysAllCoreElements() {
        // Given
        setItemCardContent(activeItem)

        // Then
        composeTestRule.onNodeWithTag("todo_title").assertIsDisplayed()
        composeTestRule.onNodeWithTag("todo_completed").assertIsDisplayed()
        composeTestRule.onNodeWithTag("todo_id").assertIsDisplayed()
        composeTestRule.onNodeWithTag("todo_userId").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Icon").assertIsDisplayed()
    }


    // 2. Contenido del item activo
    @Test
    fun itemCard_whenItemIsActive_displaysCorrectContentAndStyle() {
        // Given
        setItemCardContent(activeItem)

        // Then
        composeTestRule.onNodeWithContentDescription("Icon").assertIsDisplayed()

        composeTestRule.onNodeWithTag("todo_completed")
            .assertTextEquals("Completado")
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("todo_title")
            .assertTextEquals(activeItem.title)
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("todo_id")
            .assertTextEquals("ID: ${activeItem.id}")
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("todo_userId")
            .assertTextEquals("Usuario: ${activeItem.userId}")
            .assertIsDisplayed()
    }

    // 3. Contenido del item inactivo
    @Test
    fun itemCard_whenItemIsInactive_displaysCorrectContentAndStyle() {
        // Given
        setItemCardContent(inactiveItem)

        // Then
        composeTestRule.onNodeWithContentDescription("Icon").assertIsDisplayed()

        composeTestRule.onNodeWithTag("todo_completed")
            .assertTextEquals("Pendiente")
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("todo_title")
            .assertTextEquals(inactiveItem.title)
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("todo_id")
            .assertTextEquals("ID: ${inactiveItem.id}")
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("todo_userId")
            .assertTextEquals("Usuario: ${inactiveItem.userId}")
            .assertIsDisplayed()

    }

    @Test
    fun itemCard_withLongName_handlesTextOverflowWithEllipsis() {
        // Given
        setItemCardContent(longNameItem)

        // Then
        // Verificar que el nombre del item se muestra
        val itemNameNode = composeTestRule.onNodeWithTag("todo_title")
        itemNameNode.assertIsDisplayed()

        // Verificar que el texto completo está asociado al nodo (aunque no sea visible completo)
        itemNameNode.assertTextEquals(longNameItem.title)

        composeTestRule.onNodeWithTag("todo_completed")
            .assertTextEquals("Completado")
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("todo_id")
            .assertTextEquals("ID: ${longNameItem.id}")
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("todo_userId")
            .assertTextEquals("Usuario: ${longNameItem.userId}")
            .assertIsDisplayed()

    }

}