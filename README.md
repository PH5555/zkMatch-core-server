# 🚀 zkMatch

영지식증명(ZKP)과 탈중앙화 신원증명(DID)으로 개인정보 노출 없이 자격을 검증하고, AI로 기업과 인재를 잇는 차세대 채용 플랫폼

## 📖 Table of Contents

  - [Introduction](https://www.google.com/search?q=%23introduction)
  - [Features](https://www.google.com/search?q=%23features)
  - [Architecture](https://www.google.com/search?q=%23Architecture)
  - [Tech Stack](https://www.google.com/search?q=%23tech-stack)
  - [Acknowledgement](https://www.google.com/search?q=%23acknowledgement)
  - [License](https://www.google.com/search?q=%23license)

## 🌟 Introduction

**zkMatch**는 영지식증명(Zero-Knowledge Proof)과 탈중앙화 신원증명(Decentralized Identifier) 기술을 통해 채용 시장의 패러다임을 혁신하는 플랫폼입니다. 기존 채용 과정에서 발생했던 개인정보 과다 노출, 자격 증명의 번거로움, 그리고 채용 과정의 편견 문제를 해결합니다.

지원자는 자신의 민감한 정보를 공개하지 않으면서도 자격과 경력을 완벽하게 증명할 수 있으며, 기업은 AI 매칭을 통해 검증된 핵심 인재를 효율적으로 찾을 수 있습니다. zkMatch는 기술을 통해 모든 참여자에게 투명하고 공정한 기회를 제공하는 것을 목표로 합니다.

## ✨ Features

  - **DID 연동 이력서 생성 (Decentralized Resume)**

      - 사용자는 자신의 DID(탈중앙화 신원증명)와 연동하여 위변조가 불가능한 신뢰도 높은 디지털 이력서를 생성하고 관리할 수 있습니다.
      - 모든 경력과 자격 증명은 블록체인 상에 기록되어 투명하게 검증됩니다.

  - **영지식증명(ZK-SNARK) 기반 자격 검증 (Zero-Knowledge Proof Verification)**

      - 지원자는 자신의 학력, 경력, 연봉 등 민감한 개인정보를 직접 노출하지 않고도 기업이 요구하는 채용 조건을 충족하는지 수학적으로 증명할 수 있습니다.
      - 기업은 지원자의 프라이버시를 완벽하게 보호하면서 필요한 자격 요건 충족 여부만을 정확하게 확인할 수 있습니다.

  - **AI 채용 공고 생성 및 인재 매칭 (AI-Powered Job Matching)**

      - 기업은 간단한 키워드나 직무 설명만으로 AI가 최적의 채용 공고를 자동으로 생성해주는 기능을 활용할 수 있습니다.
      - AI가 영지식증명으로 검증된 지원자의 데이터를 분석하여, 편견 없이 직무에 가장 적합한 인재를 빠르고 정확하게 매칭합니다.
   
## 📐 Architecture

<img width="2285" height="1308" alt="Frame 1" src="https://github.com/user-attachments/assets/c3e58080-c9c8-4dc2-9cb2-77230bfd1bdf" />

[이메일 서버 리포지토리](https://github.com/PH5555/zkMatch-email-server)

## 🛠️ Tech Stack

### Backend

  - Java 21
  - Spring Boot 3.5.3
  - Spring Security + JWT
  - Spring Data JPA
  - QueryDSL 5.0.0
  - FeignClient
  - Jackson 2.17.1

### Database

  - MySQL 8
  - Redis

### Infrastructure

  - Amazon S3

### Etc

  - com.google.zxing 3.5.3

## 🙏 Acknowledgement

본 프로젝트는 탈중앙화 신원증명(DID) 기능 구현을 위해 **Open DID (OmniOne)** 프로젝트를 사용하고 있습니다. 투명하고 안전한 신원 인증 기술을 제공해준 OmniOne 커뮤니티에 감사드립니다.

  - **OmniOneID GitHub**: [https://github.com/OmniOneID](https://github.com/OmniOneID)

## 📜 License

This project is licensed under the Apache License 2.0.

  - **License**: Apache 2.0
