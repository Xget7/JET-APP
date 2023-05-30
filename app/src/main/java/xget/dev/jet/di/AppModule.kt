package xget.dev.jet.di


import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import xget.dev.jet.data.remote.auth.AuthService
import xget.dev.jet.data.remote.auth.AuthServiceImpl
import xget.dev.jet.data.repository.auth.AuthRepositoryImpl
import xget.dev.jet.data.util.network.ConnectivityImpl
import xget.dev.jet.data.util.token.TokenImpl
import xget.dev.jet.domain.repository.auth.AuthRepository
import xget.dev.jet.domain.repository.network.ConnectivityInterface
import xget.dev.jet.domain.repository.token.Token
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {





    @Provides
    @Singleton
    fun provideToken(@ApplicationContext appContext: Context): Token {
        return TokenImpl(context = appContext)
    }

    @Provides
    @Singleton
    fun provideNetworkRepository(@ApplicationContext appContext: Context): ConnectivityInterface {
        return ConnectivityImpl(appContext)
    }

    @Provides
    @Singleton
    fun provideAuthService(client: HttpClient, token: Token,): AuthService {
        return AuthServiceImpl(token = token, client = client)
    }


    @Provides
    @Singleton
    fun provideAuthRepository(authService: AuthService, token: Token,): AuthRepository {
        return AuthRepositoryImpl(authService,token)
    }
//
//    @Provides
//    @Singleton
//    fun provideFamilyRepository(fb: FirebaseFirestore): FamilyRepository {
//        return FamilyRepositoryImpl(fb)
//    }
//
//    @Provides
//    @Singleton
//    fun provideProjectRepository(fb: FirebaseFirestore): ProjectRepository {
//        return ProjectRepositoryImpl(fb)
//    }
//
//    @Provides
//    @Singleton
//    fun provideFormRepository(fb: FirebaseFirestore): FormRepository {
//        return FormRepositoryImpl(fb)
//    }
//
//    @Provides
//    @Singleton
//    fun provideGroupRepository(fb: FirebaseFirestore): GroupRepository {
//        return GroupRepositoryImpl(fb)
//    }
//
//

//
//
//    @Provides
//    @Singleton
//    fun provideFirebaseStorageRepository(storage: FirebaseStorage): FirebaseStorageRepository {
//        return FirebaseStorageRepositoryImpl(storage)
//    }
//
//    @Provides
//    @Singleton
//    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
//        return context.getSharedPreferences("local", MODE_PRIVATE)
//    }
//
//    @Provides
//    @Singleton
//    fun provideOfflineImageRepository(
//        dao: FormsDao,
//        @ApplicationContext c: Context
//    ): OfflineFormsRepository {
//        return OfflineImageRepositoryImpl(dao, c)
//    }
//
//    @Provides
//    @Singleton
//    fun provideOVisitsRepository(dao: VisitDao): FamilyLocalRepository {
//        return FamilyLocalRepositoryImpl(dao)
//    }
//
//    @Provides
//    @Singleton
//    fun provideUsersUseCases(userRepo: UsersRepository): UserUseCases {
//        return UserUseCases(
//            getUserTypeUseCase = GetUserTypeUseCase(userRepo),
//            getUserUseCase = GetUserUseCase(userRepo),
//            createUserUseCase = CreateUserUseCase(userRepo),
//            getUserForSignatureUseCase = GetUserForSignatureUseCase(userRepo),
//            getUserSnapshotUseCase = GetUserSnapshotUseCase(userRepo),
//            getUsersUseCase = GetUsersUseCase(userRepo),
//            updateUserUseCase = UpdateUserUseCase(userRepo),
//            deleteUserUseCase = DeleteUserUseCase(userRepo)
//        )
//    }
//
//    @Provides
//    @Singleton
//    fun provideFamilyUseCases(repo: FamilyRepository): FamiliesUseCases {
//        return FamiliesUseCases(
//            createFamilyUseCase = CreateFamilyUseCase(repo),
//            updateFamilyUseCase = UpdateFamilyUseCase(repo),
//            createVisitUseCase = CreateVisitUseCase(repo),
//            getFamiliesUseCase = GetFamiliesUseCase(repo),
//            getFamilyUseCase = GetFamilyUseCase(repo),
//            checkFamilyExist = CheckFamilyExist(repo)
//
//        )
//    }
//
//    @Provides
//    @Singleton
//    fun provideProjectUseCases(repo: ProjectRepository): ProjectUseCases {
//        return ProjectUseCases(
//            getProjectUseCase = GetProjectUseCase(repo),
//            createProjectUseCase = CreateProjectUseCase(repo),
//            getProjectByParticipant = GetProjectByParticipant(repo),
//            getTownsById = GetTownsById(repo),
//            getTownById = GetTownById(repo),
//            getTowns = GetTowns(repo),
//            getProjects = GetProjectsUseCase(repo),
//            updateProjectUseCase = UpdateProjectUseCase(repo),
//            updateArrayField = UpdateArrayField(repo),
//        )
//    }
//
//    @Provides
//    @Singleton
//    fun provideGroupsUseCases(repo: GroupRepository): GroupsUseCases {
//        return GroupsUseCases(
//            getGroupUseCase = GetGroupUseCase(repo),
//            createGroupUseCase = CreateGroupUseCase(repo),
//            getGroupListUseCase = GetGroupListUseCase(repo),
//        )
//    }
//
//    @Provides
//    @Singleton
//    fun provideFormsUseCases(repo: FormRepository): FormsUseCases {
//        return FormsUseCases(
//            createFormUseCase = CreateFormUseCase(repo),
//            getFormById = GetFormByIdUseCase(repo),
//            updateFormUseCase = UpdateFormUseCase(repo),
//            getFormsUseCase = GetFormsUseCase(repo),
//            getFormsSnapshotUseCase = GetFormsSnapshotUseCase(repo),
//            checkFormExist = CheckFormExist(repo)
//        )
//    }
//
//
//    @Provides
//    @Singleton
//    fun provideStorageUseCases(
//        repo: FirebaseStorageRepository,
//        offlineFormsRepository: OfflineFormsRepository
//    ): FirebaseStorageUseCases {
//        return FirebaseStorageUseCases(
//            downloadFileUseCase = DownloadFileUseCase(repo),
//            uploadImageUseCase = UploadImageUseCase(repo, offlineFormsRepository)
//        )
//    }
//
//
//    @Provides
//    @Singleton
//    fun provideFilesProvider(@ApplicationContext context: Context): FilesProvider {
//        return FilesProvider(context)
//    }
//
//
//    @Provides
//    @Singleton
//    fun provideLocationUtils(appModule: Application): LocationUtils {
//        return LocationUtils(appModule)
//    }


}