package com.kaspersky.kaspresso.sample_kautomator.test

import android.Manifest
import android.app.Instrumentation
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.kaspersky.components.kautomator.KautomatorConfigurator
import com.kaspersky.kaspresso.sample_kautomator.MainActivity

import com.kaspersky.kaspresso.sample_kautomator.screen.MainScreen
import com.kaspersky.kaspresso.systemsafety.SystemDialogSafetyProviderImpl
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * The simple sample of how Kautomator looks and
 *     its beautiful possibility to intercept all actions and assertions as Kakao does
 */
@RunWith(AndroidJUnit4::class)
class UiSimpleTest : TestCase() {

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

    @Test
    fun upgradeTest() {
        before {
            KautomatorConfigurator {
                intercept {
                    onUiInteraction {
                        onCheck { uiInteraction, uiAssert ->
                            testLogger.i("KautomatorIntercept", "interaction=$uiInteraction, assertion=$uiAssert")
                            uiInteraction.check(uiAssert)
                        }
                        onPerform { uiInteraction, uiAction ->
                            testLogger.i("KautomatorIntercept", "interaction=$uiInteraction, action=$uiAction")
                        }
                    }
                }
            }

            activityTestRule.launchActivity(null)
        }.after {
        }.run {

            step("Input text in EditText and check it") {

                val instrumentation: Instrumentation = InstrumentationRegistry.getInstrumentation()
                val uiDevice = UiDevice.getInstance(instrumentation)

                val uiObject = uiDevice.wait(
                    Until.findObject(
                        By.res(
                        "com.kaspersky.kaspresso.sample_kautomator",
                        "editText"
                        )
                    ),
                    2_000
                )
                uiObject.text = "Kaspresso"
                assertEquals(uiObject.text, "Kaspresso")

                MainScreen {
                    simpleEditText {
                        replaceText("Kaspresso")
                        hasText("Kaspresso")
                    }
                }
            }
            step("Click button") {
                MainScreen {
                    simpleButton {
                        click()
                    }
                }
            }
            step("Click checkbox and check it") {
                MainScreen {
                    checkBox {
                        setChecked(true)
                        isChecked()
                    }
                }
            }
        }
    }
}
