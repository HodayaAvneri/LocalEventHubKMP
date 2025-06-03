import SwiftUI
import GoogleMaps
import Firebase


@main
struct iOSApp: App {
    init() {
        GMSServices.provideAPIKey("AIzaSyAp5_RPiBr2ZhCI-2wIl3EVwoG2r_j52sc")
        FirebaseApp.configure()
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
