# Meu Freela

Este é um aplicativo de exemplo desenvolvido para demonstrar o uso do Firebase e a exibição de trabalhos e freelancers relacionados.

## Funcionalidades

O aplicativo possui as seguintes funcionalidades:

1. Exibição de Trabalhos:
   - Ao abrir o aplicativo, é exibida uma lista de trabalhos.
   - Cada trabalho exibe sua data e horário.
   - O nome dos freelancers relacionados a cada trabalho é exibido abaixo das informações do trabalho.

2. Visualização de Freelancers:
   - Ao selecionar um trabalho na lista, é exibida uma tela com os freelancers associados a esse trabalho.
   - Os freelancers são exibidos em forma de lista, apresentando seus nomes.

3. Integração com Firebase:
   - Os dados dos trabalhos e freelancers são armazenados no Firebase Realtime Database.
   - Os trabalhos de um usuário específico são carregados a partir do banco de dados.
   - Os freelancers relacionados a cada trabalho são carregados e exibidos no aplicativo.

4. Autenticação de Usuários:
   - O aplicativo utiliza a biblioteca FirebaseAuth para autenticar os usuários.
   - É necessário fazer login com uma conta de usuário válida para acessar o aplicativo.
   - Cada usuário tem acesso apenas aos seus próprios trabalhos e freelancers relacionados.

## Configuração

1. Clone o repositório para sua máquina local.
2. Importe o projeto no Android Studio.
3. Configure o Firebase no projeto:
   - Crie um projeto no Firebase Console (https://console.firebase.google.com) e siga as instruções para adicionar o aplicativo Android ao projeto.
   - Baixe o arquivo de configuração google-services.json fornecido pelo Firebase e coloque-o no diretório "app" do seu projeto.
   - No arquivo build.gradle do módulo do aplicativo, adicione a dependência do Firebase e o plugin do Google Services:

     ```groovy
     // Firebase
     implementation 'com.google.firebase:firebase-database:20.0.1'
     implementation 'com.google.firebase:firebase-auth:21.0.1'

     // Plugin do Google Services
     apply plugin: 'com.google.gms.google-services'
     ```

4. Execute o aplicativo em um emulador ou dispositivo Android.

## Apresentação

Você pode assistir à apresentação do projeto neste [link](https://drive.google.com/file/d/1h42mgOL0PdD1HJ9xn1Q-s8EvWWd_6LCz/view?usp=drive_link).
https://drive.google.com/file/d/1h42mgOL0PdD1HJ9xn1Q-s8EvWWd_6LCz/view?usp=drive_link

