# 🤖 Android Service - Tecnonutri Fill Service

Serviço de Acessibilidade Android para automação do preenchimento de refeições no app Tecnonutri.

## 📋 Descrição

Este módulo contém o código Kotlin de um serviço de acessibilidade que:
- Recebe dados de refeições via Intent ou notificação
- Abre o app Tecnonutri automaticamente
- Preenche formulários de alimentos usando AccessibilityService
- Salva cada item da refeição sequencialmente

## 🏗️ Arquitetura

```
TecnonutriFillService
├── Recebe JSON de refeição
├── Parseia itens (alimento, quantidade, unidade)
├── Abre Tecnonutri
└── Para cada item:
    ├── Clica em "Adicionar"
    ├── Busca alimento
    ├── Seleciona primeiro resultado
    ├── Preenche quantidade
    └── Salva
```

## 📦 Arquivos

- **TecnonutriFillService.kt** - Serviço principal de acessibilidade
- **accessibility_service_config.xml** - Configuração do serviço
- **build.gradle** - Dependências e configuração do projeto Android

## 🚀 Como Usar

### Opção 1: Usar com Tasker + AutoInput (Recomendado)

Este método não requer compilar o código Kotlin. Use os perfis Tasker fornecidos em `tasker-profiles/`.

### Opção 2: Compilar como App Standalone

Se preferir usar o serviço de acessibilidade standalone:

1. **Criar projeto Android Studio:**
```bash
# Crie um novo projeto Android
# Copie os arquivos deste diretório para o projeto
```

2. **Adicionar ao AndroidManifest.xml:**
```xml
<service
    android:name=".TecnonutriFillService"
    android:permission="android.permission.BIND_ACCESSIBILITY_SERVICE"
    android:exported="true">
    <intent-filter>
        <action android:name="android.accessibilityservice.AccessibilityService" />
    </intent-filter>
    <meta-data
        android:name="android.accessibilityservice"
        android:resource="@xml/accessibility_service_config" />
</service>
```

3. **Adicionar permissões:**
```xml
<uses-permission android:name="android.permission.QUERY_ALL_PACKAGES" />
<uses-permission android:name="android.permission.INTERNET" />
```

4. **Compilar e instalar:**
```bash
./gradlew assembleDebug
adb install app/build/outputs/apk/debug/app-debug.apk
```

5. **Ativar serviço de acessibilidade:**
   - Configurações → Acessibilidade
   - Procure "Tecnonutri Fill Service"
   - Ative o serviço

## 🧪 Testar o Serviço

### Teste via Intent (ADB)

```bash
# Enviar dados de teste
adb shell am startservice \
  -n com.taskers.meal.assistant/.TecnonutriFillService \
  --es meal_json '{"refeicao":"almoco","itens":[{"alimento":"frango grelhado","quantidade":150,"unidade":"g"}],"dataHora":"2025-11-01T13:00:00"}'
```

### Teste via Tasker

Use o perfil "Preencher Tecnonutri (Join Trigger)" em `tasker-profiles/voz_refeicao.prf.xml`.

## 📝 Formato do JSON

```json
{
  "refeicao": "almoco",
  "itens": [
    {
      "alimento": "frango grelhado",
      "quantidade": 150,
      "unidade": "g"
    },
    {
      "alimento": "arroz integral",
      "quantidade": 120,
      "unidade": "g"
    }
  ],
  "observacao": "refeição leve",
  "dataHora": "2025-11-01T13:15:00-03:00"
}
```

## 🔧 Customização

### Alterar IDs de UI

Os IDs dos elementos do Tecnonutri podem mudar entre versões. Para atualizar:

1. Use **UI Automator Viewer** (Android SDK Tools):
```bash
uiautomatorviewer
```

2. Abra o Tecnonutri no dispositivo conectado

3. Capture a tela e identifique os IDs corretos

4. Atualize as constantes em `TecnonutriFillService.kt`:
```kotlin
companion object {
    private const val ID_FAB_ADD = "com.android.systemui:id/fab"
    private const val ID_SEARCH_FIELD = "android.widget.EditText"
    private const val ID_QUANTITY_FIELD = "br.com.tecnonutri:id/quantity"
    private const val ID_SAVE_BUTTON = "br.com.tecnonutri:id/save_button"
}
```

### Adicionar Delays Personalizados

Ajuste os delays se o Tecnonutri estiver lento:

```kotlin
handler.postDelayed({
    // Ação
}, 2000) // Aumentar de 1000ms para 2000ms
```

## ⚠️ Limitações

- **Requer Acessibilidade**: O usuário deve conceder permissões de acessibilidade
- **Dependente de UI**: Quebra se o Tecnonutri mudar layout drasticamente
- **Sequencial**: Processa um item por vez (não paralelo)
- **Sem validação**: Não verifica se alimento foi encontrado corretamente

## 🔐 Segurança

- Dados de refeição não são armazenados no dispositivo
- Apenas acessa o app Tecnonutri (filtrado por package name)
- Não coleta informações pessoais
- Logs podem ser desativados removendo chamadas `Log.d()`

## 📚 Referências

- [Android Accessibility Service](https://developer.android.com/guide/topics/ui/accessibility/service)
- [AccessibilityNodeInfo](https://developer.android.com/reference/android/view/accessibility/AccessibilityNodeInfo)
- [UI Automator](https://developer.android.com/training/testing/other-components/ui-automator)

## 🐛 Debug

### Ativar logs detalhados

```bash
# Ver logs em tempo real
adb logcat -s TecnonutriFillService:V
```

### Verificar serviço ativo

```bash
adb shell settings get secure enabled_accessibility_services
```

### Forçar restart do serviço

```bash
# Desabilitar
adb shell settings put secure enabled_accessibility_services ""

# Habilitar
adb shell settings put secure enabled_accessibility_services com.taskers.meal.assistant/.TecnonutriFillService
```
