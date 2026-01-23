# 🎬 Flint

> OTT 콘텐츠 추천 및 관리 플랫폼

FLINT는 알고리즘 대신 누군가의 시선과 맥락이 담긴 **컬렉션**을 통해 시청 동기를 자극하는 OTT 콘텐츠 탐색 서비스입니다. 작품의 단순 정보가 아닌 **매력 포인트**에 집중한 큐레이션으로 정보 과잉에 지친 사용자의 탐색 피로를 획기적으로 줄여줍니다. 사용자는 끌리는 컬렉션을 수집하고 직접 생성하며, 파편화된 시청 이력을 나만의 고유한 **취향 키워드**로 축적할 수 있습니다. 단순히 볼거리를 추천하는 것을 넘어, 사용자가 스스로 '무엇에 끌리는 사람인지' 발견하고 정의하는 경험을 제공합니다.

<br/>

## 👥 Contributors
| <img src="https://github.com/user-attachments/assets/2a7ce16b-46f6-4447-920b-6692ad594d04" width="140" /> | <img src="https://github.com/user-attachments/assets/b0a27858-6ad2-47c5-af32-1981e05292f8" width="140" /> | <img src="https://github.com/user-attachments/assets/76065ce6-2098-44bd-8c61-c4cf8b86cca7" width="140" /> | <img src="https://github.com/user-attachments/assets/8340174d-c7ab-4c0d-95bd-d56af5d2b916" width="140" /> | <img src="https://github.com/user-attachments/assets/2d4fe236-ea88-43b6-b40e-d8e041d2cbe1" width="140" /> |
|:---:|:---:|:---:|:---:|:---:|
| **[김나현](https://github.com/contributor1)** | **[김종우](https://github.com/contributor3)** | **[박찬미](https://github.com/contributor4)** | **[임차민](https://github.com/contributor5)** | **[김준서](https://github.com/contributor2)** |
| 프로필, 컬렉션 목록 | 홈, 로그인 | 컬렉션 생성 | 스플레시, 온보딩 | 탐색, 컬렉션 상세 |

<br/>

## 🛠 Tech Stack

### Architecture
- **Google App Architecture** (Data - Domain - Presentation)
- **MVVM Pattern**
- **Repository Pattern**

### Android
- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Dependency Injection**: Hilt
- **Asynchronous**: Coroutines, Flow
- **Network**: Retrofit, OkHttp, kotlinx.serialization
- **Local Storage**: DataStore
- **Navigation**: Type-safe Navigation Component

## 📦 Project Structure
```

com.flint/
├── core/
│   ├── common/
│   │   ├── di/
│   │   ├── datastore/
│   │   ├── extension/
│   │   ├── manager/
│   │   └── util/
│   ├── designsystem/
│   │   ├── theme/
│   │   └── component/
│   └── navigation/
│
├── data/
│   ├── api/
│   ├── dto/
│   │   ├── base/
│   │   └── {feature}/
│   │       ├── request/
│   │       └── response/
│   ├── di/
│   └── local/
│
├── domain/
│   ├── model/
│   ├── mapper/
│   ├── repository/
│   └── type/
│
└── presentation/
     └── {feature}/
            ├── *Route.kt    
            ├── *Screen.kt         
            ├── *ViewModel.kt      
            └── *UiState.kt       
```


