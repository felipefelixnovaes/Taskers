
🧠 Projeto: Assistente de Cadastro de Refeições com IA

Automação de registro de refeições por voz no app Tecnonutri (Android)


---

📋 Sumário

1. Visão Geral


2. Arquitetura do Sistema


3. Componentes Principais


4. Fluxo de Operação


5. Tecnologias e Ferramentas


6. Configuração do Ambiente


7. Detalhamento Técnico


8. Segurança e Privacidade


9. Roadmap de Evolução


10. Licença e Créditos




---

🧭 1. Visão Geral

O projeto Assistente de Cadastro de Refeições com IA tem como objetivo automatizar o processo de lançamento de refeições no aplicativo Tecnonutri, utilizando comandos de voz e inteligência artificial para interpretação e preenchimento automático.

O usuário apenas fala o que comeu (“anotei 150g de frango, 120g de arroz e salada”), e o sistema interpreta, estrutura os dados e preenche o Tecnonutri automaticamente.


---

🏗️ 2. Arquitetura do Sistema

flowchart LR
    A[Usuário fala: "anotei 150g frango..."] --> B[Reconhecimento de Voz (STT)]
    B --> C[Parser IA (n8n + Whisper/OpenAI)]
    C --> D[JSON Estruturado da Refeição]
    D --> E[Envio para Android (Join/FCM)]
    E --> F[Tasker + AutoInput]
    F --> G[(App Tecnonutri)]
    G --> H[Confirmação por voz]

Descrição dos blocos:

STT (Speech-to-Text): Transforma a fala em texto.

Parser IA (n8n + Whisper): Interpreta o texto e gera JSON com estrutura lógica (alimento, quantidade, unidade, refeição).

Tasker + AutoInput: Recebe o JSON e executa ações automatizadas no Android.

Tecnonutri: App alvo onde os dados são cadastrados.



---

⚙️ 3. Componentes Principais

Componente	Função	Plataforma

Tasker	Gatilhos, execução de ações e integração com AutoInput	Android
AutoInput (Plugin)	Simula toques, digitação e preenchimento de campos	Android
n8n	Pipeline de IA, parsing e orquestração de dados	Nuvem ou local
Whisper / OpenAI	Transcrição de voz para texto e compreensão semântica	Nuvem / Local
Join / FCM	Envio de notificações e dados JSON para o Android	Android
Tecnonutri	Aplicativo de registro de refeições (alvo da automação)	Android



---

🔄 4. Fluxo de Operação

1. Ativação por voz:
O usuário diz algo como:

> “Anotei 150g de frango grelhado, 120g de arroz branco e salada.”




2. Transcrição (STT):
O áudio é convertido em texto pelo motor de voz do Android ou pelo Whisper.


3. Interpretação (IA Parser):
O texto é analisado por IA, que gera um JSON estruturado:

{
  "refeicao": "almoco",
  "itens": [
    {"alimento": "frango grelhado", "quantidade": 150, "unidade": "g"},
    {"alimento": "arroz branco", "quantidade": 120, "unidade": "g"},
    {"alimento": "salada", "quantidade": 1, "unidade": "porção"}
  ],
  "dataHora": "2025-11-01T13:10:00-03:00"
}


4. Envio ao Android:
O JSON é enviado via Join ou Firebase Cloud Messaging (FCM).


5. Automação de Tela (Tasker + AutoInput):

Abre o Tecnonutri;

Seleciona “+” → Refeição → Almoço;

Pesquisa cada alimento → define quantidade → salva.



6. Confirmação:
O sistema responde por voz ou notificação:

> “Seu almoço foi cadastrado com sucesso!”






---

🧰 5. Tecnologias e Ferramentas

Categoria	Ferramenta	Descrição

Automação Android	Tasker	Criação de gatilhos e ações automatizadas
Simulação de Input	AutoInput Plugin	Preenche e clica dentro do app
Voz para Texto	Whisper (OpenAI) / SpeechRecognizer	Transcrição de fala
IA e Parsing	n8n + GPT / JSON Transformer	Interpretação semântica e estruturação
Envio Android	Join App / FCM	Comunicação entre servidor e dispositivo
Aplicativo alvo	Tecnonutri	App de nutrição e registro alimentar



---

🧩 6. Configuração do Ambiente

📱 No Android

1. Instalar:

Tasker

AutoInput

Join (para receber notificações JSON)

Tecnonutri



2. Permissões:

Acessibilidade (para AutoInput)

Microfone (para STT)

Sobreposição de tela (para AutoInput)



3. Importar perfil do Tasker:

Profile: “Comando de Voz - Refeição”

Task: “Transcrever e Preencher”

Ajustar gatilho (“anotei”, “registrar”, “lançar refeição”).





---

💻 No Servidor (ou PC com n8n)

1. Criar um Webhook de entrada: /voz-refeicao


2. Conectar ao Whisper/OpenAI para transcrever o áudio.


3. Criar um nó Function com o parser de texto → JSON.


4. Enviar o JSON ao Join API:

https://joinjoaomgcd.appspot.com/_ah/api/messaging/v1/sendPush?apikey=SEU_API_KEY


5. Payload:

{
  "title": "Nova refeição",
  "text": "JSON de refeição recebido",
  "deviceId": "celular_felipe",
  "smallicon": "ic_restaurant",
  "json": "{...}"
}




---

💻 7. Detalhamento Técnico

🧩 Estrutura JSON Final

{
  "refeicao": "almoco",
  "itens": [
    {"alimento": "frango grelhado", "quantidade": 150, "unidade": "g"},
    {"alimento": "arroz branco", "quantidade": 120, "unidade": "g"},
    {"alimento": "salada", "quantidade": 1, "unidade": "porção"}
  ],
  "observacao": "refeição leve e sem molhos",
  "dataHora": "2025-11-01T13:10:00-03:00"
}

🧩 Regex de extração (fallback local)

(\d+[.,]?\d*)\s*(g|gramas|ml|x|colheres|fatia|porção|porções)?\s*([a-zA-Zçãõáéíóúêôâ ]+)

🧩 Comandos de voz aceitos

“Anotei 200g frango grelhado e 100g arroz integral.”

“Lançar almoço com 1 pão francês e 1 café preto.”

“Registrar jantar 2 fatias queijo, 1 copo leite.”



---

🔐 8. Segurança e Privacidade

Nenhum dado sensível é armazenado sem consentimento.

O áudio pode ser processado localmente (Whisper.cpp) ou na nuvem.

Tokens da API Join e OpenAI são armazenados em variáveis de ambiente.

Comunicação entre n8n e Android via HTTPS.

Logs de alimentos e horários ficam apenas no dispositivo.



---

🚀 9. Roadmap de Evolução

Etapa	Descrição	Status

v1.0	Voz → Transcrição → AutoInput (local)	✅ Concluído
v1.1	Integração n8n + Join (voz remota)	🔄 Em andamento
v1.2	Treinamento de dicionário personalizado (com IA local)	🧩 Planejado
v1.3	Interface visual para editar e confirmar refeições	🧩 Planejado
v2.0	API própria para integração com qualquer app de nutrição	🧩 Futuro



---

⚖️ 10. Licença e Créditos

Projeto open-source de automação pessoal.

Desenvolvido por Felipe Felix Novaes, automação e IA.

Licença: MIT License (uso livre, responsabilidade do usuário).



---

🧩 Extras (para repositório)

📁 assistente-refeicao-ia/
├── README.md
├── /n8n-flows/
│   ├── fluxo_refeicao.json
├── /tasker-profiles/
│   ├── voz_refeicao.prf.xml
├── /assets/
│   ├── diagrama_fluxo.png
│   ├── exemplo_json.png
└── /android-service/
    ├── TecnonutriFillService.kt


---

Quer que eu gere o README.md formatado com Markdown (com 
