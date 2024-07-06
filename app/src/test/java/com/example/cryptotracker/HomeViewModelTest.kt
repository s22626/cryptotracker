package com.example.cryptotracker

import com.example.cryptotracker.ui.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var mockPortfolioManager: PortfolioManagerInterface

    @Mock
    private lateinit var mockCurrencyService: CurrencyServiceI

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        runBlocking {
            `when`(mockCurrencyService.getPLNForUSDRate()).thenReturn(3.8)
        }
        homeViewModel = HomeViewModel(mockPortfolioManager, mockCurrencyService)
    }

    @Test
    fun whenViewModelIsInitializedItFetchesTheCurrentExchangeRate() = runTest {
        `when`(mockCurrencyService.getPLNForUSDRate()).thenReturn(3.8)
        assertEquals(3.8, homeViewModel.rate, 0.001)
    }

    @Test
    fun whenRemoveCoinIsCalledItRemovesTheCoinFromThePortfolio() = runTest {
        homeViewModel.removeCoin("Bitcoin")
        verify(mockPortfolioManager).removeCoin("Bitcoin")
    }
}