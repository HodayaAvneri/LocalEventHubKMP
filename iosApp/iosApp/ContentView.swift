import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    @State var scaleAmount = 0.0
    @State var isHomeRootScreen = false
    @State private var showImage = false
    
    var body: some View {
        ZStack {
            if isHomeRootScreen {
                ComposeView()
            } else {
                Color(red: 233/255, green: 30/255, blue: 99/255)
                    .ignoresSafeArea()
                
                if showImage {
                    Image(.logoo)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .scaleEffect(scaleAmount)
                        .frame(width: 80)
                        .onAppear {
                            withAnimation(.easeIn(duration: 1)) {
                                scaleAmount = 1.0
                            }
                            
                            DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                                isHomeRootScreen = true
                            }
                        }
                }
            }
        }
        .ignoresSafeArea(isHomeRootScreen ? .keyboard : .all)
        .onAppear {
            // Wait a short moment before showing the image
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.3) {
                showImage = true
            }
        }
    }
}



