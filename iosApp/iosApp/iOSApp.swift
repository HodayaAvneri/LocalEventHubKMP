import SwiftUI
import GoogleMaps


@main
struct iOSApp: App {
    init() {
        GMSServices.provideAPIKey("AIzaSyAp5_RPiBr2ZhCI-2wIl3EVwoG2r_j52sc")
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
