# 📦 Bibliotecas e Integrações Técnicas

Este documento reúne a justificativa técnica, versão e papel de cada biblioteca e ferramenta utilizada na construção do **CineApp UERN**.

---

## 1. Visão Geral das Bibliotecas e Versões

Todas as dependências foram configuradas de forma centralizada utilizando o **Gradle Version Catalog** no arquivo [`gradle/libs.versions.toml`](file:///C:/Users/Madson%20Gustavo/AndroidStudioProjects/appmoviesunidade3uern/gradle/libs.versions.toml).

```toml
[versions]
kotlin = "2.2.10"
composeBom = "2026.02.01"
ksp = "2.2.10-2.0.2"
navigationCompose = "2.8.5"
lifecycleCompose = "2.8.7"
room = "2.7.0"
retrofit = "2.11.0"
gson = "2.11.0"
coroutines = "1.9.0"
coil = "2.7.0"
```

---

## 2. Análise Detalhada por Componente

### 2.1. Kotlin Coroutines & StateFlow
- **Artefatos**: `org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0`, `org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0`
- **Por que foi escolhido?**:
  - Padrão da indústria para concorrência estruturada no Android.
  - Elimina *callbacks hell* substituindo por funções suspensas (`suspend`).
  - Utilizado em conjunto com `Dispatchers.IO` para operações de rede e banco de dados, prevenindo ANRs (*Application Not Responding*).
  - **`StateFlow` vs `LiveData`**: O `StateFlow` é nativo do Kotlin, não depende do framework Android no nível de domínio/repositório e integra-se perfeitamente com o Jetpack Compose via `collectAsState()`.

---

### 2.2. Jetpack Compose & Material Design 3
- **Artefatos**: Compose BoM `2026.02.01`, `androidx.compose.material3:material3`, `androidx.compose.material:material-icons-extended`
- **Por que foi escolhido?**:
  - Interface declarativa moderna que reduz em mais de 60% o volume de código em comparação com o antigo sistema de XML e Views.
  - Recomposição inteligente: apenas os nós que sofreram mutação de estado são redesenhados.
  - O **Material 3 (Material You)** oferece os componentes mais recentes de design do Google, como `CenterAlignedTopAppBar`, `CardDefaults.cardColors()`, `OutlinedTextField` com cantos arredondados e suporte nativo a temas claros/escuros.

---

### 2.3. Navigation Compose
- **Artefato**: `androidx.navigation:navigation-compose:2.8.5`
- **Por que foi escolhido?**:
  - Permite construir fluxos de navegação desacoplados em um único `NavHost`, mantendo uma única Activity (`Single Activity Architecture`).
  - Permite passagem de argumentos tipados (como o ID do filme na rota `movie_details/{movieId}`).
  - Integração limpa com o botão de voltar do sistema operacional e animações nativas de transição.

---

### 2.4. Room Database & KSP (Kotlin Symbol Processing)
- **Artefatos**: `androidx.room:room-runtime:2.7.0`, `androidx.room:room-ktx:2.7.0`, `androidx.room:room-compiler:2.7.0`
- **Plugin**: `com.google.devtools.ksp:2.2.10-2.0.2`
- **Por que foi escolhido?**:
  - Abstração type-safe sobre o SQLite com verificação de consultas SQL em tempo de compilação.
  - Suporte nativo a Coroutines (`suspend fun`) e `Flow`.
  - **Por que KSP e não KAPT?**: O KSP lê a árvore de sintaxe do Kotlin diretamente, sem gerar código intermediário em Java (*Java stubs*). Isso resulta em compilações até **2x mais rápidas** e compatibilidade total com o Kotlin 2.x.
  - **Por que Room 2.7.0?**: É a versão estável mais moderna que resolveu problemas de assinaturas de retorno JVM (`unexpected jvm signature V`) com o KSP2.

---

### 2.5. Retrofit & Gson
- **Artefatos**: `com.squareup.retrofit2:retrofit:2.11.0`, `com.squareup.retrofit2:converter-gson:2.11.0`, `com.google.code.gson:gson:2.11.0`
- **Por que foi escolhido?**:
  - Padrão consolidado no mercado para clientes HTTP REST.
  - Converte chamadas de rede em interfaces tipadas do Kotlin com anotações declarativas (`@GET`, `@Query`).
  - Integração transparente com Coroutines: métodos suspensos executam a requisição em background de forma segura.
  - O **Gson** foi utilizado para mapear as chaves com inicial maiúscula da OMDb API (`Title`, `Year`, `imdbID`) para propriedades *camelCase* do Kotlin usando `@SerializedName`.

---

### 2.6. Lifecycle ViewModel & Runtime Compose
- **Artefatos**: `androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7`, `androidx.lifecycle:lifecycle-runtime-compose:2.8.7`
- **Por que foi escolhido?**:
  - Provê a função `viewModel()` dentro dos Composables, garantindo que a ViewModel seja retida enquanto o escopo da tela estiver ativo.
  - Fornece o `viewModelScope`, que cancela automaticamente operações pendentes caso o usuário saia da tela, evitando vazamento de memória (*memory leaks*).

---

### 2.7. Coil (Coroutine Image Loader)
- **Artefato**: `io.coil-kt:coil-compose:2.7.0`
- **Por que foi escolhido?**:
  - Biblioteca moderna e leve de carregamento de imagens construída especificamente para Kotlin e Jetpack Compose.
  - Oferece o composable `AsyncImage` com suporte automático a:
    - Cache em memória e em disco para os pôsteres dos filmes.
    - Redimensionamento e corte adaptativo (`ContentScale.Crop`).
    - Tratamento de imagens ausentes ou nulas.
    - Execução 100% não bloqueante baseada em Coroutines.

---

## 3. Configurações Especiais de Build

### 3.1. Permissão de Internet
No arquivo [`app/src/main/AndroidManifest.xml`](file:///C:/Users/Madson%20Gustavo/AndroidStudioProjects/appmoviesunidade3uern/app/src/main/AndroidManifest.xml):
```xml
<uses-permission android:name="android.permission.INTERNET" />
```
Necessária para as chamadas HTTP do Retrofit à API da OMDb e para o Coil baixar as imagens dos pôsteres da CDN.

### 3.2. Configuração Gradle (`gradle.properties`)
Para manter a compatibilidade entre o Android Gradle Plugin mais recente e a geração de código fonte do KSP, configuramos:
```properties
kotlin.code.style=official
android.disallowKotlinSourceSets=false
```
Isso permite que as fontes geradas pelo KSP sejam indexadas e compiladas pelo compilador Kotlin sem atritos.
