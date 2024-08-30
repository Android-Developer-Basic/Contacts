//
//  UrlOpener.swift
//  iosApp
//
//  Created by Nikolay Kochetkov on 30/08/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import ComposeApp

@objc
class IosCommunication: NSObject, Communication {
    @objc func sendMail(address: String) {
        var components = URLComponents()
        components.scheme = "mailto"
        components.path = address
        NSLog("Opening mail app for %@", address)
        
        openUrl(components: components)
    }
    
    @objc func callPhone(phone: String) {
        var components = URLComponents()
        components.scheme = "tel"
        components.path = phone
        NSLog("Opening phone app for %@", phone)

        openUrl(components: components)
    }
    
    private func openUrl(components: URLComponents) {
        guard let url = components.url else {
            NSLog("Failed to create url with path %@", components.path)
            return
        }

        NSLog("Opening URL: %@", url.absoluteString)

        if (!UIApplication.shared.canOpenURL(url)) {
            NSLog("Can't open a client for given url")
            return
        }
        
        UIApplication.shared.open(url) { success in
            if (success) {
                NSLog("Opened client for url")
            } else {
                NSLog("Failed to open client for url")
            }
        }
    }
}

extension String {
    func utf8DecodedString()-> String {
        let data = self.data(using: .utf8)
        let message = String(data: data!, encoding: .nonLossyASCII) ?? ""
        return message
    }
    
    func utf8EncodedString()-> String {
        let messageData = self.data(using: .nonLossyASCII)
        let text = String(data: messageData!, encoding: .utf8) ?? ""
        return text
    }
}
