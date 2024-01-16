package com.example.kelineyt.paging.fail

//class MainActivity1 : AppCompatActivity() {
//
//    private val mainViewModel by viewModels<MainViewModel>()
//    private val countryAdapter = CountryAdapter()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        findViewById<RecyclerView>(R.id.rvListCountry).adapter = countryAdapter
//
//        lifecycleScope.launch {
//            mainViewModel.flow.collect {
//                countryAdapter.submitData(it)
//            }
//        }
//
//        lifecycleScope.launch {
//            countryAdapter.loadStateFlow.collectLatest { loadStates ->
//                findViewById<ProgressBar>(R.id.progressBar).isVisible = loadStates.refresh is LoadState.Loading
//                findViewById<ProgressBar>(R.id.progressBarLoadMore).isVisible = loadStates.append is LoadState.Loading
//            }
//        }
//    }
//}